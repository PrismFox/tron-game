package view.screens;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ScreenHandler implements IScreenHandler{

    private ITronView view = new TronView();

    public ScreenHandler(Stage primaryStage) throws IOException {
        StartScreen startScreen = new StartScreen("../resources/menu.css", view);
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
                StartScreen startScreen = new StartScreen("../resources/menu.css", view);
                view.registerOverlay("start", startScreen);

                view.init();
                view.showOverlay("start");
                break;
            case 2:
                WaitingScreen waitingScreen = new WaitingScreen("../resources/menu.css", view);
                view.registerOverlay("waitingScreen", waitingScreen);

                view.init();
                view.showOverlay("waitingScreen");
                break;
            case 3:
                System.err.println("wrong param");

        }
    }

    @Override
    public void showScreen(int screenNumber, int timeSec, int playerIdJoined) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("../resources/menu.css", view);
                view.registerOverlay("start", startScreen);

                view.init();
                view.showOverlay("start");
                break;
            case 2:
                WaitingScreen waitingScreen = new WaitingScreen("../resources/menu.css", view);
                view.registerOverlay("waitingScreen", waitingScreen);

                view.init();
                view.showOverlay("waitingScreen");
                break;
            case 3:
                System.err.println("wrong param");

        }
    }

    @Override
    public void showScreen(int screenNumber, int winningNumber) {

        //int uebergeben, 1-4. 1 = startscreen etc.
        //switch case, je nach int wird dann
        //view.registerOverlay(SCREEN), view.init(), view.showOverlay(SCREEN)

        switch (screenNumber){
            case 1:
                StartScreen startScreen = new StartScreen("../resources/menu.css", view);
                view.registerOverlay("start", startScreen);

                view.init();
                view.showOverlay("start");
                break;
            case 2:
                WaitingScreen waitingScreen = new WaitingScreen("../resources/menu.css", view);
                view.registerOverlay("waitingScreen", waitingScreen);

                view.init();
                view.showOverlay("waitingScreen");
                break;
            case 3:
                System.err.println("wrong param");
                break;
            case 4:
                EndScreen endScreen = new EndScreen("../resources/menu.css", view, winningNumber);
                view.registerOverlay("endScreen", endScreen);

                view.init();
                view.showOverlay("endScreen");
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
                StartScreen startScreen = new StartScreen("../resources/menu.css", view);
                view.registerOverlay("start", startScreen);

                view.init();
                view.showOverlay("start");
                break;
            case 2:
                WaitingScreen waitingScreen = new WaitingScreen("../resources/menu.css", view);
                view.registerOverlay("waitingScreen", waitingScreen);

                view.init();
                view.showOverlay("waitingScreen");
                break;
            case 3:
                //gameScreen wird erstellt, im konstruktur wird die draw() methode aufgerufen.
                view.hideOverlays();
                view.clear(); // ich weiß nicht, ob man das hier braucht, da man nur die neuen werte mitschickt und nicht die kompletten werte, bzw. komplette koordinaten.
                GameScreen gameScreen = new GameScreen("../resources/menu.css", view, bikePos);

                break;
            case 4:
                System.err.println("wrong param");
                break;
        }
    }

}
