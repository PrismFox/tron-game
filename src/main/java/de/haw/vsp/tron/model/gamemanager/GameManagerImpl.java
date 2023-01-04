package de.haw.vsp.tron.model.gamemanager;

import de.haw.vsp.tron.model.board.IBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.lobby.IInitLobby;

@Component
public class GameManagerImpl implements IGameManager {

    @Autowired
    @Lazy
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
        if(lobbyInitializer.getCurrentPlayerCount() > 1 && lobbyInitializer.getCurrentPlayerCount() <= config.getPlayerCount()) {
            return true;
        }
        return false;
    }

    @Override
    public void startGame() {
        board.initBoard();

    }

    @Override
    public void updateView(int screenNumber){
        lobbyInitializer.updateView(screenNumber);
    }

    @Override
    public void removePlayers(){
        lobbyInitializer.setCurrentPlayerCount(0);
    }

    @Override
    public void endGame() {
        removePlayers();
        lobbyInitializer.updateView(1);  
    }

    
}
