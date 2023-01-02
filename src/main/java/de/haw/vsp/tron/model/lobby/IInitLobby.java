package de.haw.vsp.tron.model.lobby;


public interface IInitLobby {

    public void initLobby();

    public void playerJoin(int playerNumber);

    public int getPlayerCount();
}
