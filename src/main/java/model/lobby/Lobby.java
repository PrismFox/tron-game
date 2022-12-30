package model.lobby;

import Enums.Color;
import model.config.IConfig;
import model.gameLogic.IGameLogic;
import model.player.IPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import view.screens.IScreenHandler;

import java.util.List;
import java.util.Map;

@Component
public class Lobby implements ILobbyGameLogic, IInitLobby{
    @Autowired
    IGameLogic gameLogic;
    @Autowired
    IScreenHandler screenHandler;
    @Autowired
    IPlayerManager playerManager;
    @Autowired
    IConfig config;

    int playerCounter = 0;
    boolean maxPlayerJoined = false;
    int maxPlayer;
    Map<Integer, List<String>> playerMapping;

    {
        assert config != null;
        maxPlayer = config.getPlayerCount();
        playerMapping = config.getPlayerMappings();
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
        if(winnerStatus[0] == 0){
            screenHandler.showScreen(4, 0);
        }else{
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
        for(Map.Entry<Integer, List<String>> entry : playerMapping.entrySet()){
            if(entry.getKey() == playerCounter){
                playerManager.createPlayer(entry.getValue(), playerCounter);
            }
        }

        if(playerCounter == maxPlayer || playerCounter == playerNumber){
            maxPlayerJoined = true;
        }

        int timeSec = config.getLobbyTimerDuration();
        screenHandler.showScreen(2, timeSec, playerCounter, maxPlayerJoined);
    }

    public int setPlayerCount(){
        int playerCount = config.getPlayerCount();
        return playerCount;
    }
}
