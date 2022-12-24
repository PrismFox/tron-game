package model.config;

import java.util.Map;

public interface IConfig {
    public Map<String, Integer> getPlayerMappings();
    public int getLobbyTimerDuration();

    //PlayerCount = wie viele Spieler erlaubt sind
    public int getPlayerCount();

    int[] getBoardSize();

    int getCellSize();
}
