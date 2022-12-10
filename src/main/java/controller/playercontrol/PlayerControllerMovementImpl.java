package controller.playercontrol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.player.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PlayerControllerMovementImpl implements IPlayerController {

    @Autowired
    private PlayerManager playerManager;
    private Map<Integer, String[]> playerMappings;

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
        Integer playerId = null;

        for (Map.Entry<Integer,String[]> entry: this.playerMappings.entrySet()){
            if (Arrays.asList(entry.getValue()).contains(key)){
                playerId = entry.getKey();
            }
        }

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
