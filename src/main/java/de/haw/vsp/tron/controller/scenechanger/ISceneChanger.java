package de.haw.vsp.tron.controller.scenechanger;

import de.haw.vsp.tron.middleware.annotation.AsyncCall;
import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;
import de.haw.vsp.tron.middleware.annotation.RemoteInterface;

@RemoteInterface
public interface ISceneChanger {
    @AsyncCall
    public void changeToNextScene();
    @AsyncCall
    public void commitAndChangeToNextScene(int startPlayerCounter);
    @AsyncCall
    public void changeToPreviousScene();
}