package view.screens;

import controller.scenechanger.ISceneChanger;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class WaitingScreen extends VBox {

    @Autowired
    private ISceneChanger iSceneChanger;

    public WaitingScreen(String stylesheet, int timeSec, int playerCounter, boolean maxPlayerJoined, ITronView view){
        super(20.0);
        this.getStylesheets().add(stylesheet);


        Label labelWaiting = new Label("Waiting for other Players to join...");
        labelWaiting.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        if(maxPlayerJoined == false){

            //liste um gejointe spieler anzeigen zu lassen
            Label ListLabel = new Label("Following Players Already joined ");
            ListView<String> playerList = new ListView<>();
            playerList.setPrefSize(100, 100);
            switch (playerCounter){
                case 0: break;
                case 1:
                    playerList.getItems().addAll("Player 1" );
                    break;
                case 2:
                    playerList.getItems().addAll("Player 1", "Player 2" );
                    break;
                case 3:
                    playerList.getItems().addAll("Player 1", "Player 2","Player 3");
                    break;
                case 4:
                    playerList.getItems().addAll("Player 1", "Player 2","Player 3", "Player 4");
                    break;
                case 5:
                    playerList.getItems().addAll("Player 1", "Player 2","Player 3", "Player 4", "Player 5");
                    break;
                case 6:
                    playerList.getItems().addAll("Player 1", "Player 2","Player 3", "Player 4", "Player 5", "Player 6");
                    break;
            }

            playerList.setStyle("-fx-font-size: 12pt; -fx-font-family: 'Sans'; -fx-font-weight: bold; -fx-text-fill: blue; " +
                    "-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 10");

            this.getChildren().add(ListLabel);
            this.getChildren().add(playerList);
        } else {
            //countdown anzeigen lassen soabld alle Spieler da sind
            // Create a label to display the time remaining
            Label timeLabel = new Label();

            // Set the initial time remaining to 5 seconds
            AtomicInteger timeRemaining = new AtomicInteger(timeSec);
            timeLabel.setText(String.valueOf(timeRemaining));

            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                timeRemaining.getAndDecrement();  // okay, timeRemaining is effectively final within the if block
                timeLabel.setText(String.valueOf(timeRemaining.get()));
                if (timeRemaining.get() <= 0 ) {
                    timeline.stop();

                    view.hideOverlays();
                    //change to next scene (GameScreen)
                    iSceneChanger.changeToNextScene();
                }
            }));

            timeline.setCycleCount(Animation.INDEFINITE);
            // Start the countdown
            timeline.play();

            this.getChildren().add(timeLabel);

        }
        this.getChildren().add(labelWaiting);
    }
}
