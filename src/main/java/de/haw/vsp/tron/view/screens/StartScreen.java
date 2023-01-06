package de.haw.vsp.tron.view.screens;


import de.haw.vsp.tron.controller.scenechanger.ISceneChanger;
import de.haw.vsp.tron.controller.scenechanger.SceneChangerImpl;
import de.haw.vsp.tron.startGame.ApplicationContextUtils;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

public class StartScreen extends VBox {


    ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
    ISceneChanger iSceneChanger = appCtx.getBean("sceneChangerImpl", SceneChangerImpl.class);

    private Button btnStart;


    public StartScreen(String stylesheet, ITronView view) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        Label labelReady = new Label("Ready?");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        Label selectLabel = new Label("Please select how many Players will play from the drop down.");
        List<Integer> playerNumberList = Arrays.asList(2,3,4,5,6);
        selectLabel.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        ComboBox dropDownBox = new ComboBox(FXCollections.observableList(playerNumberList));
        dropDownBox.setStyle("-fx-font-family: 'Times New Roman';");

        final Integer[] playerNum = {0};
        EventHandler<ActionEvent> dropDownEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        playerNum[0] = (Integer) dropDownBox.getValue();
                        System.out.println(dropDownBox.getValue());
                    }
                };
        dropDownBox.setOnAction(dropDownEvent);



        btnStart = new Button("Start Game");
        btnStart.setOnAction(event -> {
            System.out.println("click!");
            view.hideOverlays();

            iSceneChanger.commitAndChangeToNextScene(playerNum[0]);
        });

        this.getChildren().add(labelReady);
        this.getChildren().add(selectLabel);
        this.getChildren().add(dropDownBox);
        this.getChildren().add(btnStart);
    }

}
