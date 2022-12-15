package model.board;

import java.util.List;

public interface IBoard {

    /**
     *
     * @param obstacles List of arrays which contain the positions of the players and shadows
     * @param mode 0 = add new positions, 1 = remove player Positions
     */
    void updateBoard(List<int[]> obstacles, int mode);

    void remainingTime(int remainingTime);

    Board initBoard();
}
