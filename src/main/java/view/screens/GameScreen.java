package view.screens;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen extends VBox implements Screen{

    public GameScreen(String styleSheet, ITronView view, Map<Integer, int[][]> bikePos){
//        super(20.0);
//        this.getStylesheets().add(styleSheet);

        drawBikes(bikePos, view);

    }

    public void drawBikes(Map<Integer, int[][]> bikePos, ITronView view){
        for (Map.Entry<Integer, int[][]> entry : bikePos.entrySet()){
            int colorNum = entry.getKey();
            String color = Enums.Color.values()[colorNum].toString();
            Color bikeColor = Color.valueOf(color);
            List<Coordinate> coordinates = new ArrayList<>();
            for(int i = 0; i < entry.getValue().length;){
                for(int j = 0; j < entry.getValue()[i].length -1;){
                    Coordinate coordinate = new Coordinate(entry.getValue()[i][j], entry.getValue()[i][j+1]);
                    coordinates.add(coordinate);
                }
            }
            view.draw(coordinates, bikeColor);
        }
    }
}
