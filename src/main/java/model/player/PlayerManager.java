package model.player;

import java.util.List;
import java.util.Map;

public interface PlayerManager {
    Map<Integer, String[]> getPlayerMappings();

    void setPlayerMove(int playerId, int direction);

    List<Player> getLivingPlayers();

    void notifyCollisions();

    void commitPlayerMoves();
}
