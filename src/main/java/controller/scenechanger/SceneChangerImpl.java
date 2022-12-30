package controller.scenechanger;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import controller.timer.ITimer;
import model.config.IConfig;
import model.gamemanager.IGameManager;

@Component
public class SceneChangerImpl {
    @Autowired
    private IGameManager gameManager;
    @Autowired
    private IConfig config;
    @Autowired
    private ITimer timer;
    private Scene currentScene;

    @PostConstruct
    private void init() {
        this.currentScene = new StartScene(this.gameManager, this.config, this.timer, (ISceneChanger) this);
    }

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
