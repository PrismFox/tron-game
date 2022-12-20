package model.lobby;

import model.config.IConfig;
import model.gameLogic.IGameLogic;
import model.player.IPlayerManager;
import view.screens.IScreenHandler;

public class Lobby implements ILobbyGameLogic, IInitLobby{

    IGameLogic gameLogic;
    IScreenHandler screenHandler;
    IPlayerManager playerManager;
    IConfig config;

    @Override
    public void endGame() {
        createWinnerScreen();
    }

    @Override
    public void createWinnerScreen() {
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
    public Lobby initLobby() {
        //TODO: logik ausdenken, wie das mit dem countdown ist.
        Long timeSec = Long.valueOf(2); //die timer zeit muss irgendwo herkommen. Timer? Config?
        screenHandler.showScreen(2, timeSec);
        return null;
    }

    @Override
    public void playerJoin(int playerId) {
        //TODO: Logik ausdenken, was genau passieren soll, wenn player da ist
        //Was soll in der view geupdatet werden?
        //wo bekomme ich das mapping fuer den Player her?
        //playerManager.createPlayer();
    }

    public int setPlayerCount(){
        int playerCount = config.getPlayerCount();
        return playerCount;
    }
}
