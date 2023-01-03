package edu.cads.bai5.vsp.tron.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Implementation of ITronView. A config files (view.properties) is read upon object creation.
 *
 * @author Daniel Sarnow (daniel.sarnow@haw-hamburg.de)
 * @version 0.1
 */
public class TronView implements ITronView {

    private Scene scene;
    private Canvas gameBoard;
    final int WIDTH;
    final int HEIGHT;
    final int ROWS;
    final int COLUMNS;
    private Rectangle fog;
    private StackPane base;
    private Map<String, Node> overlays;
    private Color gameBoardBackgroundColor;

    public TronView() throws IOException, NumberFormatException {
        this("view.properties", Color.BLUEVIOLET.darker().darker().darker().desaturate());
    }

    public TronView(String configFile) throws IOException, NumberFormatException {
        this(configFile, Color.BLUEVIOLET.darker().darker().darker().desaturate());
    }

    public TronView(String configFile, Color gameBoardBackgroundColor) throws IOException {
        this.gameBoardBackgroundColor = gameBoardBackgroundColor;
        Properties prop = new Properties();
        prop.load(new FileInputStream(configFile));

        this.WIDTH = Integer.parseInt(prop.getProperty("width"));
        this.HEIGHT = Integer.parseInt(prop.getProperty("height"));
        this.ROWS = Integer.parseInt(prop.getProperty("rows"));
        this.COLUMNS = Integer.parseInt(prop.getProperty("columns"));

        this.overlays = new HashMap<>();
        base = new StackPane();

        gameBoard = new Canvas(WIDTH,HEIGHT);
        base.getChildren().add(gameBoard);

        fog = new Rectangle(WIDTH, HEIGHT, Color.gray(0.2,0.8));
        overlays.put("fog", fog);
        base.getChildren().add(fog);

        this.scene = new Scene(base);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void init() {
        clear();
        hideOverlays();
    }

    @Override
    public void clear() {
        // Paint game board background
        GraphicsContext g = gameBoard.getGraphicsContext2D();
        g.setFill(gameBoardBackgroundColor);
        g.fillRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
    }

    @Override
    public void draw(List<Coordinate> bike, Color color) {
        if(bike == null || color == null){
            throw new NullPointerException();
        }
        for(Coordinate pos : bike){
            if(pos.x < 0 || pos.x >= COLUMNS){
                throw new IllegalArgumentException("x value out of bounds: x is " + pos.x + ", but should be 0 <= x < " + COLUMNS);
            }
            if(pos.y < 0 || pos.y >= ROWS) {
                throw new IllegalArgumentException("y value out of bounds: y is " + pos.y + ", but should be 0 <= y < " + ROWS);
            }

            // paint new bike position
            GraphicsContext g = gameBoard.getGraphicsContext2D();
            g.setFill(color); //Color.PAPAYAWHIP);
            g.fillRect(pos.x*WIDTH/COLUMNS, pos.y*HEIGHT/ROWS, WIDTH/COLUMNS, HEIGHT/ROWS);
        }
        //System.out.println("in draw bikes - in der TronView von Becke am ende der Methode");

    }

    @Override
    public <T extends Node> void registerOverlay(String name, T overlay) {
        if(name == null || overlay == null){
            throw new NullPointerException();
        }

        overlays.put(name, overlay);
        base.getChildren().add(overlay);
    }

    @Override
    public void showOverlay(String name) {
        if(!overlays.keySet().contains(name)){
            throw new IllegalArgumentException("An overlay mapped to " + name + " does not exist. Registered are " + overlays.keySet());
        }

        overlays.get("fog").setVisible(true);
        overlays.get(name).setVisible(true);
    }

    @Override
    public void hideOverlays() {
        for(Map.Entry<String,Node> entry : overlays.entrySet()){
            entry.getValue().setVisible(false);
        }
    }

    @Override
    public void highlightCell(Coordinate cell, Color bikeColor) {
        // highlight last bike position
        GraphicsContext g = gameBoard.getGraphicsContext2D();
        g.setFill(bikeColor);
        g.fillRect(cell.x*WIDTH/COLUMNS, cell.y*HEIGHT/ROWS, WIDTH/COLUMNS, HEIGHT/ROWS);
    }
}
