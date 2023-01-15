package de.haw.vsp.tron.view.screens;

import de.haw.vsp.tron.middleware.annotation.AsyncCall;
import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;
import javafx.stage.Stage;

import java.util.Map;

import de.haw.vsp.tron.middleware.annotation.RemoteInterface;

@RemoteImplementation
public interface IScreenHandler {
    /**
     * showScreen will show the screen with the given Number
     * StartScreen
     *
     *
     * @author Dominik Martin
     */
    @AsyncCall
    public void showStartScreen();


        /**
         * showScreen will show the screen with the given Number
         * EndScreen
         * @param screenNumber, 1 = StartScreen, 2 = WaitingScreen, 3 = GameScreen, 4 = EndScreen
         * @param winningNumber, 0 = Tie, 1-6 = Player 1-6
         *
         *
         * @author Dominik Martin
         */
    @AsyncCall
    public void showLobbyScreens(int screenNumber, int winningNumber) ;

    /**
     * showScreen will show the screen with the given Number
     * GameScreen
     * @param bikePos a map with the color of the player as an int and a 2d array with the xy coordinates as ints
     *                when the key is 0, then the coordinates will be deleted , so the key 0 should
     *                only bes used, if a player dies.
     *
     * @author Dominik Martin
     */
    @AsyncCall
    public void showGameScreen(Map<Integer, Integer[][]> bikePos);

    //public void init(Stage primaryStage);

   }
