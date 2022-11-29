package controller.scenechanger;

public class StartScene extends Scene {
    @Override
    public Scene changeToNextScene() {
        return new LobbyScene();
    }
}
