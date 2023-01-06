package de.haw.vsp.tron.controller.scenechanger;

public interface ISceneCallbackRegistry {
    public void registerNextSceneCallback(Runnable callback);
    public void registerPreviousSceneCallback(Runnable callback);
}
