package de.haw.vsp.tron.view.screens;

import de.haw.vsp.tron.controller.scenechanger.ISceneChanger;
import de.haw.vsp.tron.controller.scenechanger.SceneChangerImpl;
import de.haw.vsp.tron.startGame.ApplicationContextUtils;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class EndScreen extends VBox {


    //@Autowired
    //private ISceneChanger iSceneChanger;

    ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
    ISceneChanger iSceneChanger = appCtx.getBean("sceneChangerImpl", SceneChangerImpl.class);

    private Label winningLabel;

    public EndScreen(String stylesheet, ITronView view, int winningNumber) throws InterruptedException {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        switch(winningNumber){
            case 0:
                winningLabel = new Label("Tie");
                break;
            case 1:
                winningLabel = new Label("Player 1 Won!!!");
                break;
            case 2:
                winningLabel = new Label("Player 2 Won!!!");
                break;
            case 3:
                winningLabel = new Label("Player 3 Won!!!");
                break;
            case 4:
                winningLabel = new Label("Player 4 Won!!!");
                break;
            case 5:
                winningLabel = new Label("Player 5 Won!!!");
                break;
            case 6:
                winningLabel = new Label("Player 6 Won!!!");
                break;
        }

        winningLabel.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");
        this.getChildren().add(winningLabel);

        //waits 30 sec and then changes to the next scene
        Thread.sleep(30000);
        iSceneChanger.changeToNextScene();

    }
}