package de.haw.vsp.tron.view.screens;

import javafx.stage.Stage;

import java.util.Map;

public interface IScreenHandler {
    /**
     * showScreen will show the screen with the given Number
     * StartScreen
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     *
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber);

    /**
     * showScreen will show the screen with the given Number
     * WaitingScreen
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     *
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber, int timeSec, int playerIdJoined, boolean maxPlayerJoined);

        /**
         * showScreen will show the screen with the given Number
         * EndScreen
         * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
         * @param winningNumber, 0 = Tie, 1-6 = Player 1-6
         *
         *
         * @author Dominik Martin
         */
    public void showScreen(int screenNumber, int winningNumber) throws InterruptedException;

    /**
     * showScreen will show the screen with the given Number
     * GameScreen
     * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
     * @param bikePos a map with the color of the player as an int and a 2d array with the xy coordinates as ints
     *                when the key is 0, then the coordinates will be deleted , so the key 0 should
     *                only bes used, if a player dies.
     *
     * @author Dominik Martin
     */
    public void showScreen(int screenNumber, Map<Integer, int[][]> bikePos);

    public void init(Stage primaryStage);

   }
