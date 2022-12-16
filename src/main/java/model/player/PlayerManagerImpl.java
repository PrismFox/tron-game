package model.player;

import Enums.Color;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Data
@NoArgsConstructor
@Service
public class PlayerManagerImpl implements PlayerManager {
    private List<Player> players = new ArrayList<>();

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
        List<Player> rightPlayer = this.players
                .stream().filter(player -> playerId == player.getId())
                .collect(Collectors.toList());
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
        for (Player player : players) {
            player.commitMove();
        }

    }

    @Override
    public void createPlayer(List<String> mapping, int color) {
        Player newPlayer = new Player(mapping, Color.values()[color]);
        players.add(newPlayer);
    }
}
