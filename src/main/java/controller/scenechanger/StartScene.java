package controller.scenechanger;

import controller.timer.ITimer;
import model.config.IConfig;
import model.gamemanager.IGameManager;

public class StartScene extends Scene {

    private IGameManager gameManager;
    private IConfig config;
    private ITimer timer;
    private ISceneChanger sceneChanger;

    public StartScene(IGameManager gameManager, IConfig config, ITimer timer, ISceneChanger sceneChanger) {
        this.gameManager = gameManager;
        this.config = config;
        this.timer = timer;
        this.sceneChanger = sceneChanger;

        //TODO: Inits for start screen in model
    }

    @Override
    public Scene changeToNextScene() {
        return new LobbyScene(gameManager, config, timer, sceneChanger);
    }
}
