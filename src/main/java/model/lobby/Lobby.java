package model.lobby;

import lombok.Data;
import model.config.IConfig;
import model.gamelogic.IGameLogic;
import model.player.IPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import view.screens.IScreenHandler;

import java.util.List;
import java.util.Map;

@Component
@Data
public class Lobby implements ILobbyGameLogic, IInitLobby {

    private final IGameLogic gameLogic;

    private final IScreenHandler screenHandler;

    private final IPlayerManager playerManager;

    private final IConfig config;

    private Map<Integer, List<String>> playerMapping;
    private int playerCounter = 0;
    private boolean maxPlayerJoined = false;
    private int maxPlayer;


    @Autowired
    public Lobby(IGameLogic gameLogic, IScreenHandler screenHandler, IPlayerManager playerManager, IConfig config) {
        this.gameLogic = gameLogic;
        this.screenHandler = screenHandler;
        this.playerManager = playerManager;
        this.config = config;
        maxPlayer = this.config.getPlayerCount();
        playerMapping = this.config.getPlayerMappings();
    }

    @Override
    public void endGame() throws InterruptedException {
        createWinnerScreen();
    }

    @Override
    public void createWinnerScreen() throws InterruptedException {
        //getWinnerStatus aufrufen
        //int[] mit winnerStatus. wenn [0,-1], dann ist es unentschieden

        int[] winnerStatus = gameLogic.getWinnerStatus();
        if (winnerStatus[0] == 0) {
            screenHandler.showScreen(4, 0);
        } else {
            screenHandler.showScreen(4, winnerStatus[1]);
        }
    }

    @Override
    public void initLobby() {
        int timeSec = config.getLobbyTimerDuration();
        screenHandler.showScreen(2, timeSec, 0, maxPlayerJoined);
    }

    @Override
    public void playerJoin(int playerNumber) {
        playerCounter++;
        for (Map.Entry<Integer, List<String>> entry : playerMapping.entrySet()) {
            if (entry.getKey() == playerCounter) {
                playerManager.createPlayer(entry.getValue(), playerCounter);
            }
        }

        if (playerCounter == maxPlayer || playerCounter == playerNumber) {
            maxPlayerJoined = true;
        }

        int timeSec = config.getLobbyTimerDuration();
        screenHandler.showScreen(2, timeSec, playerCounter, maxPlayerJoined);
    }

    public int getPlayerCount() {
        int playerCount = config.getPlayerCount();
        return playerCount;
    }
}
