package de.haw.vsp.tron.model.gamemanager;

public interface IGameManager {
    public void playerJoin(int id);
    public void loadLobby();
    public boolean isReadyToPlay();
    public void startGame();
    public void updateView(int screenNumber);
}
