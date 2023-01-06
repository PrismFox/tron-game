package de.haw.vsp.tron.controller.playercontrol;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import de.haw.vsp.tron.controller.scenechanger.ISceneCallbackRegistry;

import java.util.List;

@Component
public class PlayerInputManagerImpl implements IPlayerInputManager {

    @Autowired
    private ISceneCallbackRegistry sceneChanger;

    @Lazy
    @Autowired
    PlayerControllerFactory playerControllerFactory;

    private IPlayerController playerController;
    
    @PostConstruct
    private void init() {
        playerController = playerControllerFactory.createPlayerController("disabled");
        sceneChanger.registerNextSceneCallback(this::switchPlayerController);
    }

    @Override
    public void onKeyPress(String key) {
        System.out.println(this.playerController.getClass());
        this.playerController.onKeyPress(key);
    }

    @Override
    public List<String> getValidKeys() {
        return playerController.getValidKeys();
    }

    //disabled -> onboarding
    //onboarding -> movement
    //onboarding -> disabled
    //movement -> disabled

    public void switchPlayerController() {
        System.out.println("Switch Player Controller");
        if(this.playerController instanceof PlayerControllerOnboardingImpl) {
            //onboarding -> movement
            //onboarding -> disabled
            this.playerController = playerControllerFactory.createPlayerController("movement");
            sceneChanger.registerNextSceneCallback(this::switchPlayerController);
        } else if(this.playerController instanceof PlayerControllerDisabledImpl) {
            this.playerController = playerControllerFactory.createPlayerController("onboarding");
            sceneChanger.registerPreviousSceneCallback(this::init);
            sceneChanger.registerNextSceneCallback(this::switchPlayerController);
        }else{
            this.playerController = playerControllerFactory.createPlayerController("disabled");
            sceneChanger.registerNextSceneCallback(this::switchPlayerController);
        }
        playerController.loadMappings();
    }
}