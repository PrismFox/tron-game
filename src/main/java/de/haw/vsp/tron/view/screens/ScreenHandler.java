package de.haw.vsp.tron.view.screens;

import de.haw.vsp.tron.view.inputHandler.InputHandler;
import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ScreenHandler implements IScreenHandler{

    private ITronView view;

    @Autowired
    private InputHandler inputHandler1;

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

        Scene scene = view.getScene();
        scene.setOnKeyPressed(inputHandler1);


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
                    view.hideOverlays();
                    view.clear();
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
                /*view.hideOverlays();
                view.clear();*/
                EndScreen endScreen = new EndScreen("menu.css", view, winningNumber);

                Platform.runLater(() -> {

                    System.out.println("endscreen wird erstellt");
                    view.hideOverlays();
                    view.clear();
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
                    //view.clear(); // ich weiß nicht, ob man das hier braucht, da man nur die neuen werte mitschickt und nicht die kompletten werte, bzw. komplette koordinaten.
                    //System.out.println("view clear for gamescreen");
                    //GameScreen gameScreen = new GameScreen("menu.css", view, bikePos);

                    System.out.println("gamescreen erstellt");

                    drawBikes(bikePos, view);


                    //view.init();
                    //view.showOverlay("gameScreen");
                });
                    break;
            case 4:
                System.err.println("wrong param");
                break;
        }
    }

    public void drawBikes(Map<Integer, int[][]> bikePos, ITronView view) {
        //view.clear();
        List<Coordinate> list = new ArrayList<>();

        //System.out.println("in draw bikes");
        for (Map.Entry<Integer, int[][]> entry : bikePos.entrySet()) {
            int colorNum = entry.getKey();
            //System.out.println("Color num : " + colorNum);
            String color = de.haw.vsp.tron.Enums.Color.values()[colorNum].toString();
            //System.out.println(color);
            Color bikeColor = Color.valueOf(color);
            List<Coordinate> coordinates = new ArrayList<>();
            //System.out.println("entrty value .length " + entry.getValue().length);
            for (int i = 0; i < entry.getValue().length; i++) {
                //System.out.println("entry value: ");

                //System.out.println("entry value: " + entry.getValue()[i][0]);
                Coordinate coordinate = new Coordinate(entry.getValue()[i][0], entry.getValue()[i][1]);
                coordinates.add(coordinate);


            }
            //0 -> [[x,y], [x1,y1,...] , 1 -> [[x,y]], 2 -> ...
            //System.out.println(coordinates.toString());
            view.draw(coordinates, bikeColor);
            //view.highlightCell(coordinates.get(1), bikeColor);
        }
    }

}
