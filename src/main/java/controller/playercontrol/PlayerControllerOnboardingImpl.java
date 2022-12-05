package controller.playercontrol;

import java.util.List;
import java.util.Map;

import model.config.IConfig;
import model.gamemanager.IGameManager;

public class PlayerControllerOnboardingImpl implements IPlayerController {

    private IConfig config;
    private IGameManager gameManager;
    private Map<String, Integer> playerMappings;

    @Override
    public void onKeyPress(String key) {
        int playerId = this.getPlayerForKey(key);
        if(playerId != -1) {
            this.gameManager.playerJoin(playerId);
        }
        
    }

    @Override
    public List<String> getValidKeys() {
        return null;
    }

    private int getPlayerForKey(String key) {
        if(this.playerMappings == null) {
            this.playerMappings = this.config.getPlayerMappings();
        }
        Integer playerId = playerMappings.get(key);
        if(playerId == null) {
            playerId = -1;
        }
        return playerId;
    }
    
}
