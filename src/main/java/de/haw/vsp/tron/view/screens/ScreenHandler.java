package de.haw.vsp.tron.view.screens;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ScreenHandler implements IScreenHandler{

    private ITronView view;

    public ScreenHandler() {}

    public void init(Stage primaryStage) {
        try {
            view = new TronView();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartScreen startScreen = new StartScreen("menu.css", view);
        view.registerOverlay("start", startScreen);

        view.init();
        view.showOverlay("start");

        primaryStage.setTitle("Tron Game");
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }

    @Override
    public void showScreen(int screenNumber) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("menu.css", view);
                Platform.runLater(() -> {
                    view.registerOverlay("start", startScreen);
                    
                    view.init();
                    view.showOverlay("start");
                });
                break;
            case 2:
                System.err.println("wrong param");
                break;
            case 3:
                System.err.println("wrong param");
                break;
            case 4:
                System.err.println("wrong param");
                break;
        }
    }

    @Override
    public void showScreen(int screenNumber, int timeSec, int playerCounter, boolean maxPlayerJoined) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("menu.css", view);
                Platform.runLater(() -> {
                    view.registerOverlay("start", startScreen);
                    
                    view.init();
                    view.showOverlay("start");
                });
                break;
            case 2:
                WaitingScreen waitingScreen = new WaitingScreen("menu.css", timeSec, playerCounter, maxPlayerJoined, view);
                Platform.runLater(() -> {

                    view.registerOverlay("waitingScreen", waitingScreen);
                    
                    view.init();
                    view.showOverlay("waitingScreen");
                });
                break;
            case 3:
                System.err.println("wrong param");
                break;
            case 4:
                System.err.println("wrong param");
                break;

        }
    }

    @Override
    public void showScreen(int screenNumber, int winningNumber) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("menu.css", view);
                Platform.runLater(() -> {

                    view.registerOverlay("start", startScreen);
                    
                    view.init();
                    view.showOverlay("start");
                });
                break;
            case 2:
                System.err.println("wrong param");
                break;
            case 3:
                System.err.println("wrong param");
                break;
            case 4:
                EndScreen endScreen = new EndScreen("menu.css", view, winningNumber);
                Platform.runLater(() -> {
                    view.registerOverlay("endScreen", endScreen);
                    
                    view.init();
                    view.showOverlay("endScreen");
                });
                break;
        }
    }


    @Override
    public void showScreen(int screenNumber, Map<Integer, int[][]> bikePos) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("menu.css", view);
                Platform.runLater(() -> {
                    view.registerOverlay("start", startScreen);
                    
                    view.init();
                    view.showOverlay("start");
                });
                break;
            case 2:
                System.err.println("wrong param");
                break;
            case 3:
                Platform.runLater(() -> {

                    //gameScreen wird erstellt, im konstruktur wird die draw() methode aufgerufen.
                    view.hideOverlays();
                    view.clear(); // ich weiß nicht, ob man das hier braucht, da man nur die neuen werte mitschickt und nicht die kompletten werte, bzw. komplette koordinaten.
                    GameScreen gameScreen = new GameScreen("menu.css", view, bikePos);
                    
                });
                    break;
            case 4:
                System.err.println("wrong param");
                break;
        }
    }

}
