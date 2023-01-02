package de.haw.vsp.tron.controller.playercontrol;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import de.haw.vsp.tron.controller.scenechanger.ISceneChanger;

import java.util.List;

@Component
public class PlayerInputManagerImpl implements IPlayerInputManager {

    @Autowired
    private ISceneChanger sceneChanger;

    @Lazy
    @Autowired
    PlayerControllerFactory playerControllerFactory;

    private IPlayerController playerController;
    
    @PostConstruct
    private void init() {
        playerController = playerControllerFactory.createPlayerController("onboarding");
        sceneChanger.registerNextSceneCallback(() -> sceneChanger.registerNextSceneCallback(() -> switchPlayerController()));
    }

    @Override
    public void onKeyPress(String key) {
        this.playerController.onKeyPress(key);
    }

    @Override
    public List<String> getValidKeys() {
        return playerController.getValidKeys();
    }

    public void switchPlayerController() {
        System.out.println("im switch player controller");
        if(this.playerController instanceof PlayerControllerOnboardingImpl) {
            this.playerController = playerControllerFactory.createPlayerController("movement");
            sceneChanger.registerNextSceneCallback(() -> switchPlayerController());
        } else {
            this.playerController = playerControllerFactory.createPlayerController("onboarding");
            //TODO: What happens when we return from the lobby?
            sceneChanger.registerNextSceneCallback(() -> sceneChanger.registerNextSceneCallback(() -> switchPlayerController()));
        }
    }
}