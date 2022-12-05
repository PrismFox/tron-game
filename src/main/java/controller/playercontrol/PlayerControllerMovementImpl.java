package controller.playercontrol;

import java.util.List;
import java.util.Map;

import model.player.IPlayerManager;

public class PlayerControllerMovementImpl implements IPlayerController {

    private IPlayerManager playerManager;
    private Map<String, Integer> playerMappings;

    @Override
    public void onKeyPress(String key) {
        int playerId = this.getPlayerForKey(key);
        int direction = this.getDirectionForKey(key);
        this.playerManager.setPlayerMove(playerId, direction);
    }

    @Override
    public List<String> getValidKeys() {
        return null;
    }

    private int getPlayerForKey(String key) {
        if(this.playerMappings == null) {
            this.playerMappings = this.playerManager.getPlayerMappings();
        }
        Integer playerId = playerMappings.get(key);
        if(playerId == null) {
            playerId = -1;
        }
        return playerId;
    }

    private int getDirectionForKey(String key) {
        //TODO: Unimplemented method stub
        return 0;
    }
} 
