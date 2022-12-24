package model.lobby;

import lombok.RequiredArgsConstructor;
import model.config.IConfig;
import model.gameLogic.IGameLogic;
import model.player.IPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import view.screens.IScreenHandler;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Lobby implements ILobbyGameLogic, IInitLobby{

    private final IGameLogic gameLogic;
    private IScreenHandler screenHandler;
    private final IPlayerManager playerManager;
    private IConfig config;
    int playerIdJoined = 0;

    @Override
    public void endGame() {
        createWinnerScreen();
    }

    @Override
    public void createWinnerScreen() {
        //getWinnerStatus aufrufen
        //int[] mit winnerStatus. wenn [0,-1], dann ist es unentschieden

        int[] winnerStatus = gameLogic.getPlayerLogic().getWinnerStatus();
        if(winnerStatus[0] == 0){
            screenHandler.showScreen(4, 0);
        }else{
            screenHandler.showScreen(4, winnerStatus[1]);
        }
    }

    @Override
    public Lobby initLobby() {
        //TODO: logik ausdenken, wie das mit dem countdown ist.
        int timeSec = 2; //die timer zeit muss irgendwo herkommen. Timer? Config?
        screenHandler.showScreen(2, timeSec, 0);
        return null;
    }

    @Override
    public void playerJoin(int playerId) {
        //TODO: Logik ausdenken, was genau passieren soll, wenn player da ist
        //wo bekomme ich das mapping fuer den Player her? Aus der Config
        //playerManager.createPlayer();
        playerIdJoined++;
        screenHandler.showScreen(2, 2, playerIdJoined);
    }

    public int setPlayerCount(){
        int playerCount = config.getPlayerCount();
        return playerCount;
    }
}
