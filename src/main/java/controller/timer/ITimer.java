package controller.timer;

public interface ITimer {
    public void startLobbyTimer(int duration, Runnable callback);
    public void startGameTimer(int duration, Runnable callback);
    public void stopGameTimer();
}
