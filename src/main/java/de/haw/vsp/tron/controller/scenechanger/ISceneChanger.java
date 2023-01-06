package de.haw.vsp.tron.controller.scenechanger;

import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;

@RemoteImplementation
public interface ISceneChanger {
    public void changeToNextScene();
    public void commitAndChangeToNextScene(int startPlayerCounter);
    public void changeToPreviousScene();
}