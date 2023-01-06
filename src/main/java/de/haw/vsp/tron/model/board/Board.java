package de.haw.vsp.tron.model.board;

import de.haw.vsp.tron.model.config.IConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import de.haw.vsp.tron.view.screens.IScreenHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Data
@Component
@Lazy
public class Board implements IBoard {

    private static final AtomicInteger nextId = new AtomicInteger();
    private int id = 0;

    private int[] boardSize; // columns and rows

    private int cellSize = 0;

    //TODO: obstacles als int[x][y] mit Anzahl der Obstacles auf dem jeweiligen Feld wäre performanter
    private List<int[]> obstacles; // x, y for each obstacle array

    @Autowired
    private IScreenHandler screenHandler;

    @Autowired
    public Board(IConfig config) {

        this.id = nextId.getAndIncrement();
        this.boardSize = config.getBoardSize();
        this.cellSize = config.getCellSize();
        this.obstacles = new ArrayList<>();

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
        try {
            screenHandler.showLobbyScreens(3, remainingTime);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Board initBoard() {
        return null;
    }


    @Override
    public void updateView(int screenNumber, int winningNumber){
        try {
            screenHandler.showLobbyScreens(screenNumber, winningNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateView(int screenNumber, Map<Integer, int[][]> bikePos){
        screenHandler.showGameScreen(screenNumber, bikePos);
    }

    @Override
    public void clearBoard(){
        obstacles.clear();
    }
}
