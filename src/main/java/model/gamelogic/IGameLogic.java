package model.gamelogic;

import java.util.List;

public interface IGameLogic {

    void startGame();

    void updateTick();

    List<int[]> checkCollision();

    void calculateStartPositions();

    IPlayerLogic getPlayerLogic();
}
