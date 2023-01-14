package de.haw.vsp.tron.model.gamemanager;

import java.util.List;

public interface IGameManager {
    public void playerJoin(List<String> playerMapping);
    public void loadLobby();
    public boolean isReadyToPlay();
    public void startGame();
    public void updateView(int screenNumber);
    public void removePlayers();
    public void endGame();
}
