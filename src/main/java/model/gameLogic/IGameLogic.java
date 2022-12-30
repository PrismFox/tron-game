package model.gameLogic;

import java.util.List;

public interface IGameLogic {

    void startGame();

    void updateTick();

    List<int[]> checkCollision();

    void calculateStartPositions();

    void endGame();

    /**
     *
     * @return A 2d array. First index represents the winning status, 0 = draw, 1 = win. The second index stands for
     * the player. If it's a draw the player value will be -1, otherwise it's the player id
     */
    int[] getWinnerStatus();

    void initBoard();

}
