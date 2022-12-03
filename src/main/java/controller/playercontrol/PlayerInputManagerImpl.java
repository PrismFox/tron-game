package controller.playercontrol;

import controller.scenechanger.ISceneChanger;

public class PlayerInputManagerImpl implements IPlayerInputManager {

    private IPlayerController playerController;
    private ISceneChanger sceneChanger;
    private PlayerControllerFactory pControllerFactory;

    @Override
    public void onKeyPress(String key) {
        this.playerController.onKeyPress(key);
    }

    public void switchPlayerController() {
        if(this.playerController instanceof PlayerControllerOnboardingImpl) {
            this.playerController = this.pControllerFactory.createPlayerController("movement");
            // TODO: Register in sceneChanger?
        } else {
            this.playerController = this.pControllerFactory.createPlayerController("onboarding");
        }
    }
}