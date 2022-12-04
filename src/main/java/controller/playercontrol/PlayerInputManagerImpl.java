package controller.playercontrol;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import controller.scenechanger.ISceneChanger;

@Component
public class PlayerInputManagerImpl implements IPlayerInputManager {

    @Autowired
    private IPlayerController playerController;
    @Autowired
    private ISceneChanger sceneChanger;

    @Override
    public void onKeyPress(String key) {
        this.playerController.onKeyPress(key);
    }

    public void switchPlayerController() {
        if(this.playerController instanceof PlayerControllerOnboardingImpl) {
            this.playerController = PlayerControllerFactory.createPlayerController("movement");
            // TODO: Register in sceneChanger?
        } else {
            this.playerController = PlayerControllerFactory.createPlayerController("onboarding");
        }
    }
}