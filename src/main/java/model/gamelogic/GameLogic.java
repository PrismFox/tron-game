package model.gamelogic;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.board.IBoard;
import model.lobby.ILobbyGameLogic;
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

    private final IPlayerLogic playerLogic;

    private final IBoard board;

    private final ILobbyGameLogic lobby;

    @Override
    public void startGame() {
        log.debug("Start of Game");
        this.calculateStartPositions();
    }

    @Override
    public void updateTick() {
        List<int[]> obstaclesToRemove = new ArrayList<>();

        playerLogic.getPlayerManager().commitPlayerMoves();
        updateBoard();
        List<int[]> collisions = checkCollision();
        List<Player> players = playerLogic.getPlayerManager().getLivingPlayers();
        Map<Integer, int[][]> viewStuff = new HashMap<>();

        if (!collisions.isEmpty()) {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                int[] currentPosition = player.getCurrentPosition();
                if (collisions.contains(currentPosition)) {
                    obstaclesToRemove.addAll(playerLogic.getPlayerManager().getPlayerPositions(player.getId()));
                    playerLogic.killPlayer(player.getId());
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
            // Game loop muss interrupted werden
        }

        if (!obstaclesToRemove.isEmpty()) {
            board.updateBoard(obstaclesToRemove, 1);
        }

        board.updateView(3, viewStuff);
    }

    private void updateBoard() {
        List<int[]> currentPositions = playerLogic.getPlayerManager().getLivingPlayers().stream()
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
        List<Player> players = getPlayerLogic().getPlayerManager().getLivingPlayers();
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
}
