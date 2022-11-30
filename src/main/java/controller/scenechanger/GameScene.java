package controller.scenechanger;

import controller.scenechanger.StartScene;

public class GameScene extends Scene {
    @Override
    public Scene changeToNextScene() {
        return new StartScene();
    }
}
