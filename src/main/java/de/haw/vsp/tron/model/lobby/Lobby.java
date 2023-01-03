package de.haw.vsp.tron.model.lobby;

import de.haw.vsp.tron.model.gamelogic.IGameLogic;
import lombok.Data;
import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.player.IPlayerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import de.haw.vsp.tron.view.screens.IScreenHandler;

import java.util.List;
import java.util.Map;

@Component
@Data
@Lazy
@Slf4j
public class Lobby implements ILobbyGameLogic, IInitLobby {

    @Autowired
    @Lazy
    private IGameLogic gameLogic;

    @Autowired
    private IScreenHandler screenHandler;

    @Autowired
    private IPlayerManager playerManager;

    private final IConfig config;

    private Map<Integer, List<String>> playerMapping;
    private int playerCounter = 0;
    private boolean maxPlayerJoined = false;
    private int maxPlayer = 0;


    @Autowired
    public Lobby(IConfig config) {
        this.config = config;
        maxPlayer = this.config.getPlayerCount();
        playerMapping = this.config.getPlayerMappings();
    }

    @Override
    public void endGame() {
        createWinnerScreen();
    }

    @Override
    public void createWinnerScreen() {
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
                log.info("Create Player {}", playerCounter);
            }
        }

        if (playerCounter == maxPlayer || playerCounter == playerNumber) {
            maxPlayerJoined = true;
        }
        //TODO timesec wieder raus nehmen
        int timeSec = config.getLobbyTimerDuration();
        screenHandler.showScreen(2, timeSec, playerCounter, maxPlayerJoined);
    }

    public int getPlayerCount() {
        int playerCount = config.getPlayerCount();
        return playerCount;
    }
}
