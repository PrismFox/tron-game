package de.haw.vsp.tron.controller.playercontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.gamemanager.IGameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerControllerOnboardingImpl implements IPlayerController {

    @Autowired
    private IConfig config;

    @Autowired
    private IGameManager gameManager;

    private Map<String, Integer> playerIdKeyMap;

    @Override
    public void onKeyPress(String key) {
        int playerId = this.getPlayerForKey(key);
        if(playerId != -1) {
            this.gameManager.playerJoin(playerId);
        }
        
    }

    @Override
    public List<String> getValidKeys() {

        if(this.playerIdKeyMap == null) {
            this.playerIdKeyMap = new HashMap<>();
            Map<Integer, List<String>> playerMappings = this.config.getPlayerMappings();
            playerMappings.entrySet().stream().forEach(
                    e -> e.getValue().forEach(
                            v -> this.playerIdKeyMap.put(v, e.getKey())
                    )
            );
        }
        return new ArrayList<>(this.playerIdKeyMap.keySet());
    }

    private int getPlayerForKey(String key) {
        if(this.playerIdKeyMap == null) {
            this.playerIdKeyMap = new HashMap<>();
            Map<Integer, List<String>> playerMappings = this.config.getPlayerMappings();
            playerMappings.entrySet().stream().forEach(
                e -> e.getValue().forEach(
                    v -> this.playerIdKeyMap.put(v, e.getKey())
                )
            );
        }
        Integer playerId = playerIdKeyMap.get(key);
        if(playerId == null) {
            playerId = -1;
        }
        return playerId;
    }
    
}
