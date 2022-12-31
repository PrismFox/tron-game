package model.gamemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.config.IConfig;
import model.lobby.IInitLobby;

@Component
public class GameManagerImpl implements IGameManager {

    @Autowired
    private IInitLobby lobbyInitializer;

    @Autowired
    private IConfig config;

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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateCountdown(String msg) {
        // TODO Auto-generated method stub
        
    }
    
}
