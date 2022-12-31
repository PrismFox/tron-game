package model.gamemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.board.IBoard;
import model.config.IConfig;
import model.lobby.IInitLobby;

@Component
public class GameManagerImpl implements IGameManager {

    @Autowired
    private IInitLobby lobbyInitializer;

    @Autowired
    private IConfig config;

    @Autowired
    private IBoard board;

    @Override
    public void playerJoin(int id) {
        lobbyInitializer.playerJoin(id);
    }

    @Override
    public void loadLobby() {
        lobbyInitializer.initLobby();
    }

    @Override
    public boolean isReadyToPlay() {
        if(lobbyInitializer.getPlayerCount() == config.getPlayerCount()) {
            return true;
        }
        return false;
    }

    @Override
    public void startGame() {
        board.initBoard();
    }

    
}
