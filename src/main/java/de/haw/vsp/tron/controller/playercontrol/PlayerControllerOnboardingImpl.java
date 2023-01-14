package de.haw.vsp.tron.controller.playercontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<Integer, List<String>> playerMappings;

    public void loadMappings() {
        this.playerIdKeyMap = new HashMap<>();
        playerMappings = this.config.getPlayerMappings();
        playerMappings.entrySet().stream().forEach(
                e -> e.getValue().forEach(
                        v -> this.playerIdKeyMap.put(v, e.getKey())));
    }

    @Override
    public void onKeyPress(String key) {

        System.out.println("{DEBUG} PlayerControllerOnbaordingImpl onKeyPress : key = " + key);
        String[] seperatedPrefix = key.split("\\|");
        String prefix;
        if(seperatedPrefix.length > 1){
            prefix = seperatedPrefix[0];
        }else{
            prefix = "";
        }
        System.out.println("{DEBUG} PlayerControllerOnbaordingImpl onKeyPress : prefix = " + prefix);

        String keyWithoutPrefix = seperatedPrefix[seperatedPrefix.length-1];
        int playerId = this.getPlayerForKey(keyWithoutPrefix);
                System.out.println("PlayerControllerOnboardingImpl: OnKeyPress: playerId : " + playerId);

        if (playerId != -1) {
            List<String> playerKeys = playerMappings.get(playerId);
            List<String> playerMappingWithPrefix = playerKeys.stream().map(elem -> String.format("%s|%s", prefix, elem)).collect(Collectors.toList());

            this.gameManager.playerJoin(playerMappingWithPrefix);
            System.out.println("PlayerControllerOnboardingImpl: OnKeyPress: playerId : " + playerId + " wurde playerJoind aufgerufen");
        }



    }

    @Override
    public List<String> getValidKeys() {
        return new ArrayList<>(this.playerIdKeyMap.keySet());
    }

    private int getPlayerForKey(String key) {
        Integer playerId = playerIdKeyMap.get(key);
        if (playerId == null) {
            playerId = -1;
        }
        return playerId;
    }

}
