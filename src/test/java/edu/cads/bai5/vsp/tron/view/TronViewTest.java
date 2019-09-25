package edu.cads.bai5.vsp.tron.view;

import javafx.scene.paint.Color;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TronViewTest {

    @Test
    public void test_creation() throws IOException {
        ITronView view = new TronView();
    }

    @Test(expected = IOException.class)
    public void test_creation_fails() throws IOException {
        ITronView view = new TronView("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_outOfBounds_01() throws IOException {
        ITronView view = new TronView();

        List<Coordinate> bike = new ArrayList<>();
        bike.add(new Coordinate(-1,0));

        view.draw(bike, Color.PAPAYAWHIP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_outOfBounds_02() throws IOException {
        ITronView view = new TronView();

        List<Coordinate> bike = new ArrayList<>();
        bike.add(new Coordinate(0,-1));

        view.draw(bike, Color.PAPAYAWHIP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_outOfBounds_03() throws IOException {
        ITronView view = new TronView();

        List<Coordinate> bike = new ArrayList<>();
        bike.add(new Coordinate(40,0));

        view.draw(bike, Color.PAPAYAWHIP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_outOfBounds_04() throws IOException {
        ITronView view = new TronView();

        List<Coordinate> bike = new ArrayList<>();
        bike.add(new Coordinate(0,40));

        view.draw(bike, Color.PAPAYAWHIP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_showOverlay_fails() throws IOException {
        ITronView view = new TronView();
        view.showOverlay("test");
    }

}