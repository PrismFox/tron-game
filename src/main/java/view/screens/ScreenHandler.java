package view.screens;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.Objects;

public class ScreenHandler extends Application implements IScreenHandler{

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ITronView view = new TronView("view.properties");

        StartScreen startScreen = new StartScreen("menu.css", view);
        view.registerOverlay("start", startScreen);

        view.init();
        view.showOverlay("start");





       // StartScreen startScreen = new StartScreen();

        //startScreen.displayMessage("dominik", primaryStage);

        primaryStage.setTitle("Tron Game");
        primaryStage.setScene(view.getScene());
        primaryStage.show();

    }

    @Override
    public void setCurrentScreen() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
