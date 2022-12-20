package model.lobby;

import model.gameLogic.GameLogic;
import model.gameLogic.IGameLogic;
import model.player.IPlayerManager;
import model.player.Player;
import view.screens.IScreenHandler;

public class Lobby implements ILobbyGameLogic, IInitLobby{

    IGameLogic gameLogic;
    IScreenHandler screenHandler;
    IPlayerManager playerManager;

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
        Long timeSec = Long.valueOf(2);
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
        //TODO hier muss ich von Config die playeranzahl raus holen die erlaubt ist, und diese zurueck geben
        //public int getPlayerCount(); muss in config sein.
        return 0;
    }
}
