package de.haw.vsp.tron.controller.scenechanger;

public interface ISceneChanger {
    public boolean changeToNextScene();
    public boolean commitAndChangeToNextScene(int startPlayerCounter);
    public boolean changeToPreviousScene();
}