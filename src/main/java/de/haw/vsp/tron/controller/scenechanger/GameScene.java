package de.haw.vsp.tron.controller.scenechanger;

import de.haw.vsp.tron.controller.timer.ITimer;
import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.gamemanager.IGameManager;

public class GameScene extends Scene {

    private IGameManager gameManager;
    private IConfig config;
    private ITimer timer;
    private ISceneChanger sceneChanger;

    public GameScene(IGameManager gameManager, IConfig config, ITimer timer, ISceneChanger sceneChanger) {
        this.gameManager = gameManager;
        this.config = config;
        this.timer = timer;
        this.sceneChanger = sceneChanger;
        this.gameManager.startGame();
    }

    @Override
    public Scene changeToNextScene() {
        return new StartScene(this.gameManager, this.config, this.timer, this.sceneChanger);
    }
}
