package model.config;

import java.util.List;
import java.util.Map;

public interface IConfig {
    public Map<Integer, List<String>> getPlayerMappings();
    public int getLobbyTimerDuration();

    //PlayerCount = wie viele Spieler erlaubt sind
    public int getPlayerCount();

    public void setPlayerCount(int playerCount);

    int[] getBoardSize();

    int getCellSize();
}
