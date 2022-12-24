package model.gameLogic;

import java.util.List;

public interface IGameLogic {

    void startGame();

    void updateTick();

    List<int[]> checkCollision();

    void calculateStartPositions();

    void endGame();

    IPlayerLogic getPlayerLogic();

    void initBoard();

}
