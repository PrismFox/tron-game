package model.player;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
public class PlayerManagerImpl implements PlayerManager {

    private List<Player> players;

    @Override
    public Map<Integer, List<String>> getPlayerMappings() {
        Map<Integer, List<String>> mapping = new HashMap<>();

        for (Player player : players) {
            mapping.put(player.getId(), player.getMapping());
        }
        return mapping;
    }

    @Override
    public void setPlayerMove(int playerId, int direction) {
        List<Player> rightPlayer = this.players.stream().filter(player -> playerId == player.getId()).toList();
        Player player = rightPlayer.get(0);
        player.setIntendedDirection(direction);
    }

    @Override
    public List<Player> getLivingPlayers() {
        List<Player> livingPlayers = new ArrayList<>();

        for (Player player : players) {
            if (player.isAlive())
                livingPlayers.add(player);
        }

        return livingPlayers;
    }

    @Override
    public void notifyCollisions(int[][] positions) {
        for (Player player : players) {
            for (int[] position : positions) {
                if (Arrays.equals(position, player.getCurrentPosition())) {
                    player.setAlive(false);
                }
            }
        }
    }

    @Override
    public void commitPlayerMoves() {
        for (Player player: players){
            player.commitMove();
        }

    }
}
