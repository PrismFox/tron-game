package model.gameLogic;

import java.util.List;

public interface GameLogic {

    void startGame();

    void updateTick();

    List<int[]> checkCollision();

    List<int[]> calculateStartPositions();

    void endGame();

    int[] getWinnerStatus();
}
