package model.gamelogic;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.board.IBoard;
import model.lobby.ILobbyGameLogic;
import model.player.IPlayerManager;
import model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Data
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GameLogic implements IGameLogic {

    private final IPlayerManager playerManager;

    private final IBoard board;

    private final ILobbyGameLogic lobby;

    @Override
    public void startGame() {
        log.debug("Start of Game");
        this.calculateStartPositions();
    }

    // todo exceptions sollten innerhalb der selben Komponente behandelt werden
    @Override
    public boolean updateTick() throws InterruptedException {
        List<int[]> obstaclesToRemove = new ArrayList<>();

        playerManager.commitPlayerMoves();
        updateBoard();
        List<int[]> collisions = checkCollision();
        List<Player> players = playerManager.getLivingPlayers();
        Map<Integer, int[][]> viewStuff = new HashMap<>();


        // todo den folgenden Part mit der Iterieung über die Player in PlayerManager auslagern
        if (!collisions.isEmpty()) {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                int[] currentPosition = player.getCurrentPosition();
                if (collisions.contains(currentPosition)) {
                    obstaclesToRemove.addAll(playerManager.getPlayerPositions(player.getId()));
                    killPlayer(player.getId());
                    players.remove(player);
                }else{
                    viewStuff.put(player.getColor().ordinal(), new int[][]{currentPosition});
                }
            }
        }


        int[][] allPositionsArray = obstaclesToRemove.toArray(new int[obstaclesToRemove.size()][]);
        viewStuff.put(0, allPositionsArray );

        if (players.size() <= 1) {
           lobby.endGame();
           log.debug("End of Game");
           return false;
            // Game loop muss interrupted werden
        }

        if (!obstaclesToRemove.isEmpty()) {
            board.updateBoard(obstaclesToRemove, 1);
        }

        board.updateView(3, viewStuff);
        return true;
    }

    private void updateBoard() {
        List<int[]> currentPositions = playerManager.getLivingPlayers().stream()
                .map(Player::getCurrentPosition)
                .collect(Collectors.toList());

        board.updateBoard(currentPositions, 0);
    }

    @Override
    public List<int[]> checkCollision() {
        int sizeBoardXY = board.getBoardSize()[0];
        List<int[]> obstacles = board.getObstacles();
        List<int[]> collisionPositions = new ArrayList<>();

        for (int[] obstacle : obstacles) {
            if (!collisionPositions.contains(obstacle)) {
                if (obstacle[0] < 0 || obstacle[0] > (sizeBoardXY - 1) || obstacle[1] < 0 ||
                        obstacle[1] > (sizeBoardXY - 1)) { // Border check
                    collisionPositions.add(obstacle);
                    log.debug(String.format("Collision detected at x: %d, y: %d", obstacle[0], obstacle[1]));
                }
                if (Collections.frequency(obstacles, obstacle) > 1) {
                    collisionPositions.add(obstacle);
                    log.debug(String.format("Collision detected at x: %d, y: %d", obstacle[0], obstacle[1]));
                }
            }
        }
        return collisionPositions;
    }

    @Override
    public void calculateStartPositions() {
        List<Player> players = playerManager.getLivingPlayers();
        int median = board.getBoardSize()[1] / 2;
        int smallMedianY = median / 2;
        int bigMedianY = median + (median / 2);
        List<Player> half1 = new ArrayList<>();
        List<Player> half2 = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (i < (players.size() + 1) / 2) {
                half1.add(players.get(i));
            } else {
                half2.add(players.get(i));
            }
        }

        setStartPositions(half1, smallMedianY);
        setStartPositions(half2, bigMedianY);
    }

    private void setStartPositions(List<Player> playerList, int yPosition) {
        int increment = 0;
        List<int[]> startPositions = new ArrayList<>();
        int incrementValue = board.getBoardSize()[0] / playerList.size();

        int[] startPosition = new int[2];
        startPosition[1] = yPosition;

        for (Player player : playerList) {
            startPosition[0] = increment;
            startPositions.add(startPosition);
            player.setCurrentPosition(startPosition);
            increment += incrementValue;
        }

        board.updateBoard(startPositions, 0);
    }


    @Override
    public void killPlayer(int playerId) {
        playerManager.killPlayer(playerId);
    }

    @Override
    public int[] getWinnerStatus() {
        List<Player> livingPlayers = getPlayerManager().getLivingPlayers();
        int[] result = new int[2];

        if (livingPlayers.isEmpty()) {

            result[1] = -1;

        }

        if (livingPlayers.size() == 1) {
            result[0] = 1;
            result[1] = livingPlayers.get(0).getId();
        }

        return result;
    }
}
