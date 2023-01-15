package de.haw.vsp.tron.controller.scenechanger;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import de.haw.vsp.tron.controller.timer.ITimer;
import de.haw.vsp.tron.model.config.IConfig;
import de.haw.vsp.tron.model.gamelogic.IGameLogic;
import de.haw.vsp.tron.model.gamemanager.IGameManager;

@Component
public class SceneChangerImpl implements ISceneChanger, ISceneCallbackRegistry {
    @Autowired
    private IGameManager gameManager;
    @Autowired
    private IConfig config;
    @Autowired
    private ITimer timer;
    private Scene currentScene;

    @Autowired
    @Lazy
    private IGameLogic gameLogic;

    private List<Runnable> previousSceneCallbacks = new ArrayList<>();
    private List<Runnable> nextSceneCallbacks = new ArrayList<>();

    @PostConstruct
    private void init() {
        this.currentScene = new StartScene(this.gameManager, this.config, this.timer, (ISceneChanger) this);

        Runnable gameRunnable = () -> {
            timer.startLobbyTimer(3000, () -> {
                gameLogic.removePlayers();
                gameManager.endGame();
                init();
            });
        };
        Runnable lobbyRunnable = () -> {
            registerNextSceneCallback(gameRunnable);
        };
        Runnable lobbyFailedRunnable = () -> {
            init();
        };
        Runnable startRunnable = () -> {
            int lobbyTimerDuration = config.getLobbyTimerDuration();
            timer.startLobbyTimer(lobbyTimerDuration, () -> {
                if(gameManager.isReadyToPlay()) {
                    gameManager.startGame();
                    gameLogic.startGame();
                    changeToNextScene();
                    timer.startGameTimer(config.getGameTick(), () -> {
                        updateTick();
                    });
                } else {
                    gameManager.updateView(1);
                    gameLogic.removePlayers();
                    gameManager.removePlayers();
                    changeToPreviousScene();
                }
            });

            this.gameManager.loadLobby();
            registerNextSceneCallback(lobbyRunnable);
            registerPreviousSceneCallback(lobbyFailedRunnable);
        };
        registerNextSceneCallback(startRunnable);
    }


    private void updateTick() {
        try {
            if(!gameLogic.updateTick()) {
                timer.stopGameTimer();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void changeToNextScene() {
        Scene newScene = currentScene.changeToNextScene();

        if(newScene != null) {
            this.currentScene = newScene;
            
            List<Runnable> callbacks = new ArrayList<>(this.nextSceneCallbacks);
            this.previousSceneCallbacks.clear();
            this.nextSceneCallbacks.clear();

            callbacks.forEach(callback -> callback.run());
    
        }
    }

    @Override
    public void commitAndChangeToNextScene(int startPlayerCounter) {
        config.setPlayerCount(startPlayerCounter);
        changeToNextScene();
    }

    @Override
    public void changeToPreviousScene() {
        Scene newScene = currentScene.changeToPreviousScene();
        
        if(newScene != null) {
            this.currentScene = newScene;

            List<Runnable> callbacks = new ArrayList<>(this.previousSceneCallbacks);
            this.previousSceneCallbacks.clear();
            this.nextSceneCallbacks.clear();

            callbacks.forEach(callback -> callback.run());
    
        }
    }

    @Override
    public void registerNextSceneCallback(Runnable callback) {
        this.nextSceneCallbacks.add(callback);
    }

    @Override
    public void registerPreviousSceneCallback(Runnable callback) {
        this.previousSceneCallbacks.add(callback);
    }
}
