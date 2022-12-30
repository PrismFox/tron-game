package model.gameLogic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.board.Board;
import model.config.Config;
import model.lobby.Lobby;
import model.player.Player;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class GameLogic implements IGameLogic {

    private Board board;

    private Config config;


    private PlayerLogic playerLogic;

    private Lobby lobby;

    public GameLogic() {
        this.playerLogic = new PlayerLogic();
        this.config = new Config();
        this.lobby = new Lobby();
        initBoard();
    }


    @Override
    public void startGame() {
        System.out.println("test");
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
            int[] winningStatus = getWinnerStatus();
            if (winningStatus[0] == 0) {
                board.updateView(4, 0);

            } else {
                board.updateView(4, winningStatus[1]);
            }

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
                if (obstacle[0] < 0 || obstacle[0] > (sizeBoardXY - 1) || obstacle[1] < 0 || obstacle[1] > (sizeBoardXY - 1)) { //Border check
                    collisionPositions.add(obstacle);
                }
                if (Collections.frequency(obstacles, obstacle) > 1) {
                    collisionPositions.add(obstacle);
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
        for (Player player : playerList) {
            startPosition[0] = increment;
            startPosition[1] = yPosition;
            startPositions.add(startPosition);
            player.setCurrentPosition(startPosition);
            increment += incrementValue;
        }

        board.updateBoard(startPositions, 0);
    }

    @Override
    public void endGame() {
        System.out.println("test");
    }

    @Override
    public int[] getWinnerStatus() {
        List<Player> livingPlayers = getPlayerLogic().getPlayerManager().getLivingPlayers();
        int[] result = new int[2];

        if (livingPlayers.size() == 0) {

            result[1] = -1;

        }

        if (livingPlayers.size() == 1) {
            result[0] = 1;
            result[1] = livingPlayers.get(0).getId();
        }

        return result;
    }

    @Override
    public void initBoard() {
        this.board = new Board(new int[]{23, 23}, 2, new ArrayList<>());
    }

}
