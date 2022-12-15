package model.player;

import java.util.List;
import java.util.Map;

public interface PlayerManager {
    Map<Integer, List<String>> getPlayerMappings();

    void setPlayerMove(int playerId, int direction);

    List<Player> getLivingPlayers();

    void notifyCollisions(int[][] positions);

    void commitPlayerMoves();
}
