package model.gameLogic;

import controller.scenechanger.GameScene;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.board.Board;
import model.config.Config;
import model.config.IConfig;
import model.lobby.Lobby;
import model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        System.out.println("test");
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

        int xIncrement = 0;
        for (int i = 0; i < half1.size(); i++) {
            half1.get(i).setCurrentPosition(new int[]{xIncrement, smallMedianY});
            xIncrement += board.getBoardSize()[0] / half1.size();
        }

        xIncrement = 0;
        for (int i = 0; i < half1.size(); i++) {
            half2.get(i).setCurrentPosition(new int[]{xIncrement, bigMedianY});
            xIncrement += board.getBoardSize()[0] / half2.size();
        }

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

        result[0] = 1;
        result[1] = livingPlayers.get(0).getId();
        return result;
    }

    public void initBoard() {
        this.board = new Board(new int[]{23, 23}, 2, new ArrayList<>());
    }

}
