package de.haw.vsp.tron.controller.scenechanger;

public interface ISceneChanger {
    public void changeToNextScene();
    public void commitAndChangeToNextScene(int startPlayerCounter);
    public void changeToPreviousScene();
}