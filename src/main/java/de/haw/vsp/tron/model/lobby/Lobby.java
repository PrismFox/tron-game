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
    private int currentPlayerCount = 0;
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
        if (winnerStatus[0] == -1) {
            screenHandler.showLobbyScreens(4, -1);
        } else {
            System.out.println("winner status ist " + winnerStatus[1]);
            screenHandler.showLobbyScreens(4, winnerStatus[1]);
        }
    }

    @Override
    public void initLobby() {
        screenHandler.showLobbyScreens(2, 0);
    }

    @Override
    public void updateView(int screenNumber) {
        screenHandler.showStartScreen();
    }

    @Override
    public void playerJoin(List<String> playerMapping) {

        currentPlayerCount++;
        System.out.println("Lobby : playerJoin: playerNumber: " + currentPlayerCount);
        playerManager.createPlayer(playerMapping, currentPlayerCount);
        System.out.println("Lobby : playerJoin: playerNumber: " + currentPlayerCount + " create Player ausgefuert");

        if (currentPlayerCount == maxPlayer) {
            maxPlayerJoined = true;
        }
        screenHandler.showLobbyScreens(2, currentPlayerCount);
    }

    @Override
    public int getCurrentPlayerCount() {
        System.out.println("PlayerCounter aus lobby" + currentPlayerCount);
        return currentPlayerCount;
    }

    @Override
    public void setCurrentPlayerCount(int currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;

    }
}
