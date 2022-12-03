package view.screens;

import javafx.stage.Stage;

public interface Screen {

    public void clearMessage();

    void displayMessage(String message, Stage primaryStage);

    public void updateView();

}
