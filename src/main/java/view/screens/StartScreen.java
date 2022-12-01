package view.screens;


import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;


public class StartScreen extends VBox implements Screen {

    private final Button btnStart;

    //Parent root = FXMLLoader.load(Paths.get("/Users/dominik/Desktop/DominikMartin/UNI/Semester_5/VS/Praktikum/tron-game/src/main/resources/test.fxml").toUri().toURL());

    Pane sp = new javafx.scene.layout.Pane();
    //Scene startScene = new Scene(root, 1000,1000);

    public StartScreen(String stylesheet, ITronView view) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        Label labelReady = new Label("Ready?");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnStart = new Button("Start Game");
        btnStart.setOnAction(event -> {
            System.out.println("click!");
            view.hideOverlays();
        });

        this.getChildren().add(labelReady);
        this.getChildren().add(btnStart);
    }

    @Override
    public void clearMessage() {

    }

    @Override
    public void displayMessage(String message, Stage primaryStage) {

        Text messageText = new Text();
        messageText.setText(message);

        messageText.setStyle("-fx-font: normal bold 50px 'serif' ");

        //sp.getChildren().add(messageText);
        messageText.setX(100);
        messageText.setY(1000);

        setScene(primaryStage);

    }

    @Override
    public void updateView() {

    }


    public void setScene(Stage primaryStage){

        //primaryStage.setScene(startScene);

    }

}
