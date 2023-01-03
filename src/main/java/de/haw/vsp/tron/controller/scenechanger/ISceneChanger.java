package de.haw.vsp.tron.controller.scenechanger;

public interface ISceneChanger {
    public boolean changeToNextScene();
    public boolean changeToNextScene(int startPlayerCounter);
    public boolean changeToPreviousScene();
    public void registerNextSceneCallback(Runnable callback);
    public void registerPreviousSceneCallback(Runnable callback);
}