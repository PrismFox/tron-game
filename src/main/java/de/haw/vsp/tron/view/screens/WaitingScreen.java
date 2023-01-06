package de.haw.vsp.tron.view.screens;


import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class WaitingScreen extends VBox {

    public WaitingScreen(String stylesheet, int playerCounter){
        super(20.0);
        this.getStylesheets().add(stylesheet);


        Label labelWaiting = new Label("Waiting for other Players to join...");
        labelWaiting.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");


            //liste um gejointe spieler anzeigen zu lassen
            Label ListLabel = new Label("Following Players Already joined ");
            ListView<String> playerList = new ListView<>();
            playerList.setPrefSize(100, 190);
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

            playerList.setStyle("-fx-font-size: 12pt; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: blue; " +
                    "-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 10");


        playerList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);

                    switch (item){
                        case "Player 1": setTextFill(Color.RED); break;
                        case "Player 2": setTextFill(Color.BLUE); break;
                        case "Player 3": setTextFill(Color.GREEN); break;
                        case "Player 4": setTextFill(Color.YELLOW); break;
                        case "Player 5": setTextFill(Color.PURPLE); break;
                        case "Player 6": setTextFill(Color.ORANGE); break;
                    }

                } else {
                    setText(null);
                }
            }
        });

            this.getChildren().add(ListLabel);
            this.getChildren().add(playerList);


        this.getChildren().add(labelWaiting);
    }
}
