package de.haw.vsp.tron.model.lobby;


import java.util.List;

public interface IInitLobby {

    public void initLobby();

    public void playerJoin(List<String> playerMapping);

    public int getCurrentPlayerCount();

    public void setCurrentPlayerCount(int currentPlayerCount);

    public void updateView(int screenNumber);

    }
