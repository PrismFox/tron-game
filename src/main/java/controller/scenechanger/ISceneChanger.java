package controller.scenechanger;

public interface ISceneChanger {
    public boolean changeToNextScene();
    public boolean changeToPreviousScene();
    public void registerNextSceneCallback(Runnable callback);
    public void registerPreviousSceneCallback(Runnable callback);
}