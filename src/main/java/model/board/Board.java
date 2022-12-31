package model.board;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import view.screens.IScreenHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Data
public class Board implements IBoard {

    private static final AtomicInteger nextId = new AtomicInteger();
    private int id;

    private int[] boardSize; // columns and rows

    private int cellSize;

    private List<int[]> obstacles; // x, y for each obstacle array

    @Autowired
    private IScreenHandler screenHandler;

    public Board(int[] boardSize, int cellSize, List<int[]> obstacles) {
        this.id = nextId.getAndIncrement();
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        this.obstacles = obstacles;

    }

    @Override
    public void updateBoard(List<int[]> obstacles, int mode) {
        if (mode == 0) {
            addObstacles(obstacles);
        }
        if (mode == 1) {
            removeObstacles(obstacles);
        }

    }

    private void addObstacles(List<int[]> obstaclesA) {
        this.obstacles.addAll(obstaclesA);

    }

    private void removeObstacles(List<int[]> obstaclesR) {
        for (int[] obstacle : obstaclesR) {
            OptionalInt obstacleIndex = IntStream.range(0, obstacles.size())
                    .filter(index -> Arrays.equals(obstacle, obstacles.get(index)))
                    .findFirst();

            if (obstacleIndex.isPresent()) {
                obstacles.remove(obstacleIndex.getAsInt());
            }
        }
    }

    @Override
    public void remainingTime(int remainingTime) {
        screenHandler.showScreen(3, remainingTime);
    }

    @Override
    public Board initBoard() {
        return null;
    }


    @Override
    public void updateView(int screenNumber, int winningNumber){
        screenHandler.showScreen(screenNumber, winningNumber);
    }

    @Override
    public void updateView(int screenNumber, Map<Integer, int[][]> bikePos){
        screenHandler.showScreen(screenNumber, bikePos);
    }
}
