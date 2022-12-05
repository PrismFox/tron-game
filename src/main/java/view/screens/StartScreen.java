package view.screens;


import controller.scenechanger.ISceneChanger;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

        Label selectLabel = new Label("Please select how many Players will play from the drop down.");
        List<Integer> playerNumberList = Arrays.asList(1,2,3,4,5,6);
        selectLabel.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        ComboBox dropDownBox = new ComboBox(FXCollections.observableList(playerNumberList));
        EventHandler<ActionEvent> dropDownEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        //TODO: Hier muss eine Methode aufgerufen werden vom controller, damit dieser weiß, wie viele player mitspielen.
                        System.out.println(dropDownBox.getValue());
                    }
                };
        dropDownBox.setOnAction(dropDownEvent);



        btnStart = new Button("Start Game");
        btnStart.setOnAction(event -> {
            System.out.println("click!");
            view.hideOverlays();
            //change to next scene (WaitingScreen)
            iSceneChanger.changeToNextScene();
        });

        this.getChildren().add(labelReady);
        this.getChildren().add(selectLabel);
        this.getChildren().add(dropDownBox);
        this.getChildren().add(btnStart);
    }

}
