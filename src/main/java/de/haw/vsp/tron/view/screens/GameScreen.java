package de.haw.vsp.tron.view.screens;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen extends VBox {

    public GameScreen(String styleSheet, ITronView view, Map<Integer, int[][]> bikePos){
        super(20.0);
        this.getStylesheets().add(styleSheet);

        drawBikes(bikePos, view);

    }

    public void drawBikes(Map<Integer, int[][]> bikePos, ITronView view){

        List<Coordinate> list = new ArrayList<>();
        list.add(new Coordinate(20,23));
        view.draw(list, Color.BLUE);
        System.out.println("in draw bikes");
        for (Map.Entry<Integer, int[][]> entry : bikePos.entrySet()){
            int colorNum = entry.getKey();
            System.out.println("Color num : " + colorNum);
            String color = de.haw.vsp.tron.Enums.Color.values()[colorNum].toString();
            System.out.println(color);
            Color bikeColor = Color.valueOf(color);
            List<Coordinate> coordinates = new ArrayList<>();
            System.out.println("entrty value .length " + entry.getValue().length);
            for(int i = 0; i < entry.getValue().length; i++){
                System.out.println("entry value: ");

                    System.out.println("entry value: "+entry.getValue()[i][0]);
                    Coordinate coordinate = new Coordinate(entry.getValue()[i][0], entry.getValue()[i][1]);
                    coordinates.add(coordinate);
                    //view.highlightCell(coordinate, );

            }
            //0 -> [[x,y], [x1,y1,...] , 1 -> [[x,y]], 2 -> ...
            System.out.println(coordinates.toString());
            view.draw(coordinates, bikeColor);
        }
    }
}
