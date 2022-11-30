package view.screens;


import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;


public class StartScreen implements Screen{

    Parent root = FXMLLoader.load(Paths.get("/Users/dominik/Desktop/DominikMartin/UNI/Semester_5/VS/Praktikum/tron-game/src/main/resources/test.fxml").toUri().toURL());

    Pane sp = new javafx.scene.layout.Pane();
    Scene startScene = new Scene(root, 1000,1000);

    public StartScreen() throws IOException {
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

        primaryStage.setScene(startScene);

    }

}
