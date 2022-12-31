package controller.playercontrol;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import controller.scenechanger.ISceneChanger;

@Component
public class PlayerInputManagerImpl implements IPlayerInputManager {

    @Autowired
    private ISceneChanger sceneChanger;
    private IPlayerController playerController = PlayerControllerFactory.createPlayerController("onboarding");
    
    @PostConstruct
    private void init() {
        playerController = PlayerControllerFactory.createPlayerController("onboarding");
        sceneChanger.registerNextSceneCallback(() -> sceneChanger.registerNextSceneCallback(() -> switchPlayerController()));
    }

    @Override
    public void onKeyPress(String key) {
        this.playerController.onKeyPress(key);
    }

    public void switchPlayerController() {
        if(this.playerController instanceof PlayerControllerOnboardingImpl) {
            this.playerController = PlayerControllerFactory.createPlayerController("movement");
            sceneChanger.registerNextSceneCallback(() -> switchPlayerController());
        } else {
            this.playerController = PlayerControllerFactory.createPlayerController("onboarding");
            //TODO: What happens when we return from the lobby?
            sceneChanger.registerNextSceneCallback(() -> sceneChanger.registerNextSceneCallback(() -> switchPlayerController()));
        }
    }
}