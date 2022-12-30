package model.gamemanager;

public interface IGameManager {
    public void playerJoin(int id);
    public void loadLobby();
    public boolean isReadyToPlay();
    public void startGame();
    public void updateCountdown(String msg);
}
