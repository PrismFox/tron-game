package model.gamelogic;

import model.player.IPlayerManager;

public interface IPlayerLogic {
    void killPlayer(int playerId);

    /**
     * @return A 2d array. First index represents the winning status, 0 = draw, 1 = win. The second index stands for
     * the player. If it's a draw the player value will be -1, otherwise it's the player id
     */
    int[] getWinnerStatus();

    IPlayerManager getPlayerManager();

}
