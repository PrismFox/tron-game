package de.haw.vsp.tron.model.config;

import java.util.List;
import java.util.Map;

public interface IConfig {
    public Map<Integer, List<String>> getPlayerMappings();
    public int getLobbyTimerDuration();

    //PlayerCount = wie viele Spieler erlaubt sind
    public int getPlayerCount();

    public void setPlayerCount(int playerCount);

    public int[] getBoardSize();

    public int getCellSize();

    public int getGameTick();
}
