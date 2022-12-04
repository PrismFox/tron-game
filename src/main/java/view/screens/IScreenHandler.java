package view.screens;

import java.util.Map;

public interface IScreenHandler {

    public void showScreen(int screenNumber);

    public void showScreen(int screenNumber, int winningNumber);

    public void showScreen(int screenNumber, int winningNumber, Map<String, int[][]> bikePos);

}
