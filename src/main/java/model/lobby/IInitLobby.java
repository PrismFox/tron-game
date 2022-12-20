package model.lobby;

import model.player.Player;

public interface IInitLobby {

    public Lobby initLobby();

    public void playerJoin(int playerId);
}
