package de.haw.vsp.tron.controller.scenechanger;

import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;

@RemoteImplementation
public interface ISceneChanger {
    public boolean changeToNextScene();
    public boolean commitAndChangeToNextScene(int startPlayerCounter);
    public boolean changeToPreviousScene();
}