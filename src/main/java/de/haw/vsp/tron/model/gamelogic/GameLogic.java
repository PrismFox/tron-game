package de.haw.vsp.tron.model.gamelogic;

import de.haw.vsp.tron.model.board.IBoard;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import de.haw.vsp.tron.model.lobby.ILobbyGameLogic;
import de.haw.vsp.tron.model.player.IPlayerManager;
import de.haw.vsp.tron.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Data
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Lazy
public class GameLogic implements IGameLogic {

    private final IPlayerManager playerManager;

    @Lazy
    private final IBoard board;

    @Lazy
    private final ILobbyGameLogic lobby;

    @Override
    public void startGame() {
        log.info("Start of Game");
        this.calculateStartPositions();
    }

    // todo exceptions sollten innerhalb der selben Komponente behandelt werden
    @Override
    public boolean updateTick() throws InterruptedException {

        playerManager.commitPlayerMoves();
        updateBoard();
        List<int[]> collisions = checkCollision();

        Map<Integer, int[][]> colorPositionsMap = playerManager.checkPlayerCollision(collisions);
        int[][] obstaclesToRemoveArray = colorPositionsMap.get(0);
        List<int[]> obstaclesToRemove = Arrays.asList(obstaclesToRemoveArray);
        List<Player> currentLivingPlayers = playerManager.getLivingPlayers();

        if (currentLivingPlayers.size() <= 1) {

            try {
                System.out.println("currentplayers <= 1");
                lobby.endGame();
                log.debug("End of Game");
            } catch (InterruptedException exception) {
                log.error("Lobby couldn't end the Game");
                throw exception;
            }

            return false;
            // Game loop muss interrupted werden
        }

        if (!obstaclesToRemove.isEmpty()) {
            board.updateBoard(obstaclesToRemove, 1);
        }
        System.out.println("view screen 3 aufgerufen");
        board.updateView(3, colorPositionsMap);
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
            //if (!collisionPositions.stream().anyMatch(collision -> Arrays.equals(collision, obstacle))) {
                if (obstacle[0] < 0 || obstacle[0] > (sizeBoardXY - 1) || obstacle[1] < 0 ||
                        obstacle[1] > (sizeBoardXY - 1)) { // Border check
                    collisionPositions.add(obstacle);
                    log.debug(String.format("Collision detected at x: %d, y: %d", obstacle[0], obstacle[1]));
                }
                if (obstacles.stream().filter(o -> Arrays.equals(o, obstacle)).count() > 1) {
                    collisionPositions.add(obstacle);
                    log.debug(String.format("Collision detected at x: %d, y: %d", obstacle[0], obstacle[1]));
                }
            //}
        }
        return collisionPositions;
    }

    @Override
    public void calculateStartPositions() {
        System.out.println("calculate Start position");
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
        log.debug("Divide Board size{} by playersize {}", board.getBoardSize()[0], playerList.size());
        int incrementValue = board.getBoardSize()[0] / playerList.size();

        for (Player player : playerList) {
            int[] startPosition = new int[2];
            startPosition[1] = yPosition;

            startPosition[0] = increment;
            startPositions.add(startPosition);
            System.out.println(increment);
            player.setCurrentPosition(startPosition);
            increment += incrementValue;
        }

        board.updateBoard(startPositions, 0);
    }

    @Override
    public int[] getWinnerStatus() {
        List<Player> livingPlayers = getPlayerManager().getLivingPlayers();
        int[] result = new int[2];

        if (livingPlayers.isEmpty()) {
            result[0] = -1;
            result[1] = -1;

        }

        if (livingPlayers.size() == 1) {
            System.out.println("livinplayers == 1");
            result[0] = 1;
            System.out.println("result von 0 in livinplayers"+result[0]);
            result[1] = livingPlayers.get(0).getId();
        }

        return result;
    }
}
