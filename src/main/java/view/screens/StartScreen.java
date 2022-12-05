package view.screens;


import controller.scenechanger.ISceneChanger;
import controller.scenechanger.Scene;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartScreen extends VBox implements Screen {

    @Autowired
    private ISceneChanger iSceneChanger;

    private final Button btnStart;


    public StartScreen(String stylesheet, ITronView view) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        Label labelReady = new Label("Ready?");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        MenuButton numberButton = new MenuButton("Select number of Players");
        numberButton.getItems().addAll(new MenuItem("1"), new MenuItem("2"), new MenuItem("3"), new MenuItem("4"), new MenuItem("5"), new MenuItem("6"));

        //TODO onClick methode fuer die numberButtons items -> player muessen in der richtigen anzahl erzeugt werden

        btnStart = new Button("Start Game");
        btnStart.setOnAction(event -> {
            System.out.println("click!");
            view.hideOverlays();
            //change to next scene (WaitingScreen)
            iSceneChanger.changeToNextScene();
        });

        this.getChildren().add(labelReady);
        this.getChildren().add(numberButton);
        this.getChildren().add(btnStart);
    }

}
