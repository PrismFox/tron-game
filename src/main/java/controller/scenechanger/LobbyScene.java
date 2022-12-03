package controller.scenechanger;

public class LobbyScene extends Scene {
    @Override
    public Scene changeToNextScene() {
        return new GameScene();
    }
    @Override
    public Scene changeToPreviousScene() {
        return new StartScene();
    }
}
