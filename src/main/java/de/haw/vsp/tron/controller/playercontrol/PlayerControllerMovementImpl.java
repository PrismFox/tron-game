package de.haw.vsp.tron.controller.playercontrol;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.haw.vsp.tron.model.player.IPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerControllerMovementImpl implements IPlayerController {

    @Autowired
    private IPlayerManager playerManager;
    private Map<Integer, List<String>> playerMappings;
    private Map<String, Integer> playerKeyIdMap;

    @Override
    public void onKeyPress(String key) {
        int playerId = this.getPlayerForKey(key);
        int direction = this.getDirectionForKey(key);
        this.playerManager.setPlayerMove(playerId, direction);
    }

    @Override
    public List<String> getValidKeys() {
        if(this.playerMappings == null) {
            this.playerMappings = this.playerManager.getPlayerMappings();
        }
        return playerMappings.values().stream().flatMap(List::stream).collect(Collectors.toList());
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
        if(this.playerMappings == null) {
            this.playerMappings = this.playerManager.getPlayerMappings();
        }
        for (Map.Entry<Integer, List<String>> entry: this.playerMappings.entrySet()) {
            int index = entry.getValue().indexOf(key);
            if(index != -1) {
                return index;
            }
        }
        return -1;
    }
} 
