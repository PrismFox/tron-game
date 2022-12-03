package view.screens;


import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;



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

        MenuButton numberButton = new MenuButton("Select number of Players");
        numberButton.getItems().addAll(new MenuItem("1"), new MenuItem("2"), new MenuItem("3"), new MenuItem("4"), new MenuItem("5"), new MenuItem("6"));

        btnStart = new Button("Start Game");
        btnStart.setOnAction(event -> {
            System.out.println("click!");
            view.hideOverlays();
        });

        this.getChildren().add(labelReady);
        this.getChildren().add(numberButton);
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
