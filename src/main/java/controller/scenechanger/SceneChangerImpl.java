package controller.scenechanger;

import controller.timer.ITimer;
import model.config.IConfig;
import model.gamemanager.IGameManager;

public class SceneChangerImpl {
    private Scene currentScene;
    private IGameManager gameManager;
    private IConfig config;
    private ITimer timer;

    public boolean changeToNextScene() {
        Scene newScene = currentScene.changeToNextScene();
        if(newScene != null) {
            this.currentScene = newScene;
            return true;
        }
        return false;
    }

    public boolean changeToPreviousScene() {
        Scene newScene = currentScene.changeToPreviousScene();
        if(newScene != null) {
            this.currentScene = newScene;
            return true;
        }
        return false;
    }
}
