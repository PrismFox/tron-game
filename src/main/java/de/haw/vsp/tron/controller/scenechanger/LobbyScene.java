package de.haw.vsp.tron.controller.scenechanger;

import de.haw.vsp.tron.controller.timer.ITimer;
import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.gamemanager.IGameManager;

public class LobbyScene extends Scene {

    private IGameManager gameManager;
    private IConfig config;
    private ITimer timer;
    private ISceneChanger sceneChanger;
    
    public LobbyScene(IGameManager gameManager, IConfig config, ITimer timer, ISceneChanger sceneChanger) {
        this.gameManager = gameManager;
        this.config = config;
        this.timer = timer;
        this.sceneChanger = sceneChanger;
        this.initLobby();
    }

    private void initLobby() {
        this.gameManager.loadLobby();
        int duration = config.getLobbyTimerDuration();
        timer.startLobbyTimer(duration, () -> onCountdownEnd());
    }

    private void onCountdownEnd() {
        if(this.gameManager.isReadyToPlay()) {
            this.sceneChanger.changeToNextScene();
        } else {
            this.sceneChanger.changeToPreviousScene();
        }
    }

    @Override
    public Scene changeToNextScene() {
        return new GameScene(gameManager, config, timer, sceneChanger);
    }
    @Override
    public Scene changeToPreviousScene() {
        return new StartScene(gameManager, config, timer, sceneChanger);
    }
}
