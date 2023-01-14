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

    @Override
    public void onKeyPress(String key) {
        System.out.println("PlayerControllerMovementImpl: onKeyPress: key = " + key);


        int playerId = this.getPlayerForKey(key);
        int direction = this.getDirectionForKey(key);
        this.playerManager.setPlayerMove(playerId, direction);
    }

    @Override
    public List<String> getValidKeys() {
        return playerMappings.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    private int getPlayerForKey(String key) {
        Integer playerId = null;

        for (Map.Entry<Integer,List<String>> entry: this.playerMappings.entrySet()){
            System.out.println("PlayerControllerMovementImpl : Map Key = " + entry.getKey().toString() + " value = " + entry.getValue().toString());
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
        for (Map.Entry<Integer, List<String>> entry: this.playerMappings.entrySet()) {
            int index = entry.getValue().indexOf(key);
            if(index != -1) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void loadMappings() {
        this.playerMappings = this.playerManager.getPlayerMappings();
    }
} 
