package view.screens;

import java.util.Map;

public interface IScreenHandler {
    /**
     * showScreen will show the screen with the given Number
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     *
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber);

    /**
     * showScreen will show the screen with the given Number
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     * @param winningNumber, 0 = Tie, 1-6 = Player 1-6
     *
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber, int winningNumber);

    /**
     * showScreen will show the screen with the given Number
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     * @param bikePos a map with the color of the player as a string and an 2d array witht the xy coordinates as ints
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber, int winningNumber, Map<String, int[][]> bikePos);

}
