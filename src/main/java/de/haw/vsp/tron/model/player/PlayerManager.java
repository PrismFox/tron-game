package de.haw.vsp.tron.model.player;

import de.haw.vsp.tron.Enums.Color;
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
public class PlayerManager implements IPlayerManager {
    private List<Player> players = new ArrayList<>();

    public List<Player> getPlayers() {
        return players;
    }


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
        setIntendedDirection(playerId, direction);
        //ist<Player> rightPlayer = this.players
        //        .stream().filter(player -> playerId == player.getId())
        //        .collect(Collectors.toList());
        //Player player = rightPlayer.get(0);
        //player.setIntendedDirection(direction);

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
        System.out.println("Player ID bei Create Player in PlayerManager " +newPlayer.getId());
        players.add(newPlayer);
    }

    @Override
    public void setIntendedDirection(int playerId, int intendedDirection) {
        Player player = getPlayerById(playerId);
        player.setIntendedDirection(intendedDirection);
        System.out.println("Player " + playerId + " Direction " + intendedDirection);
    }

    @Override
    public Player getPlayerById(int playerId) {
        Player resultPlayer = null;
        for (Player player : players) {
            if (player.getId() == playerId) {
                resultPlayer = player;
            }
        }
        return resultPlayer;
    }

    @Override
    public List<int[]> getPlayerPositions(int playerId) {
        Player player = getPlayerById(playerId);
        List<int[]> allPositions = new ArrayList<>();
        allPositions.add(player.getCurrentPosition());
        allPositions.addAll(player.getShadows());

        return allPositions;
    }

    @Override
    public void killPlayer(int playerId) {
        Player player = getPlayerById(playerId);
        player.setAlive(false);
    }

    public Map<Integer, int[][]> checkPlayerCollision(List<int[]> collisions) {
        List<int[]> obstaclesToRemove = new ArrayList<>();
        Map<Integer, int[][]> colorPositions = new HashMap<>();
        List<Player> livingPlayers = getLivingPlayers();

        //if (!collisions.isEmpty()) {
            for (int i = 0; i < livingPlayers.size(); i++) {
                Player player = livingPlayers.get(i);
                int[] currentPosition = player.getCurrentPosition();
                if (collisions.stream().anyMatch(c -> Arrays.equals(c, currentPosition))) {
                    obstaclesToRemove.addAll(getPlayerPositions(player.getId()));
                    killPlayer(player.getId());
                } else {
                    colorPositions.put(player.getColor().ordinal(), new int[][]{currentPosition});
                }
            }
        //}


        int[][] allPositionsArray = obstaclesToRemove.toArray(new int[obstaclesToRemove.size()][]);
        colorPositions.put(0, allPositionsArray);

        return colorPositions;
    }

    @Override
    public void killPlayers(){
        Player.resetNextId();
        players.clear();

    }
}
