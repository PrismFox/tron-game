package model.player;

import java.util.Map;

public interface IPlayerManager {
    public Map<String, Integer> getPlayerMappings();
    public void setPlayerMove(int playerId, int direction);
}
