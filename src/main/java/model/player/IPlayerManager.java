package model.player;


import java.util.List;
import java.util.Map;


public interface IPlayerManager {
    Map<Integer, List<String>> getPlayerMappings();

    void setPlayerMove(int playerId, int direction);

    List<Player> getLivingPlayers();

    void notifyCollisions(int[][] positions);

    void commitPlayerMoves();

    void createPlayer(List<String> mapping, int color);

    void setIntendedDirection(int playerId, int intendedDirection);

    List<int[]> getPlayerPositions(int playerId);

    void killPlayer(int playerId);

    Player getPlayerById(int playerId);
}
