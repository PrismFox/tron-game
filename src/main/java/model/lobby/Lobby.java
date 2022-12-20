package model.lobby;

import model.gameLogic.GameLogic;
import model.gameLogic.IGameLogic;
import model.player.Player;
import view.screens.IScreenHandler;

public class Lobby implements ILobbyGameLogic, IInitLobby{

    IGameLogic gameLogic;
    IScreenHandler screenHandler;

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
        return null;
    }

    @Override
    public void playerJoin(Player player) {

    }

    public int setPlayerCount(){
        //TODO hier muss ich von Config die playeranzahl raus holen die erlaubt ist, und diese zurueck geben

        return 0;
    }
}
