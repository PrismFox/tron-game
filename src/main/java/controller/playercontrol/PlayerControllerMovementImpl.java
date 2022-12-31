package controller.playercontrol;

import java.util.List;
import java.util.Map;

import model.player.IPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PlayerControllerMovementImpl implements IPlayerController {

    @Autowired
    private IPlayerManager playerManager;
    private Map<Integer, List<String>> playerMappings;

    @Override
    public void onKeyPress(String key) {
        int playerId = this.getPlayerForKey(key);
        int direction = this.getDirectionForKey(key);
        this.playerManager.setPlayerMove(playerId, direction);
    }

    @Override
    public List<String> getValidKeys() {
        return playerMappings.values().stream().flatMap(List::stream).toList();
    }

    private int getPlayerForKey(String key) {
        if(this.playerMappings == null) {
            this.playerMappings = this.playerManager.getPlayerMappings();
        }
        Integer playerId = null;

        for (Map.Entry<Integer,List<String>> entry: this.playerMappings.entrySet()){
            if (entry.getValue().contains(key)){
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
