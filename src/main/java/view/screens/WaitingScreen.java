package view.screens;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class WaitingScreen extends VBox implements Screen{

    public WaitingScreen(String stylesheet, ITronView view){
        super(20.0);
        this.getStylesheets().add(stylesheet);

        Label labelWaiting = new Label("Waiting for other Players to join...");
        labelWaiting.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");
        //TODO hier muss man noch einen Countdown machen, mit der Time vom Model
        //TODO hier muss noch eine Liste mit playern die gejoined sind

    }

}
