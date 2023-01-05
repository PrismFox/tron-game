package de.haw.vsp.tron.model.player;

import de.haw.vsp.tron.Enums.Color;
import de.haw.vsp.tron.Enums.Direction;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Player {

    private static final AtomicInteger nextId = new AtomicInteger(1);
    private final int id;
    private int[] currentPosition = new int[2];

    private int[] lastPosition = new int[2];
    private List<int[]> shadows = new ArrayList<>();

    private boolean alive;
    private int direction; //Actual direction
    private int intendedDirection; //Can change multiple times during a tick
    private final List<String> mapping;
    private final Color color;

    public Player(List<String> mapping, Color color) {
        this.id = nextId.getAndIncrement();
        this.mapping = mapping;
        this.color = color;
        this.alive = true;
    }


    private void commitMove(int dir) {
        if (Direction.UP == Direction.values()[dir]) {
            this.changePosition(1, 0);
        }

        if (Direction.Down == Direction.values()[dir]) {
            this.changePosition(1, 1);
        }

        if (Direction.LEFT == Direction.values()[dir]) {
            this.changePosition(0, 0);
        }

        if (Direction.RIGHT == Direction.values()[dir]) {
            this.changePosition(0, 1);
        }
        System.out.println("----- Player " + this.id + " Direction: " + Direction.values()[intendedDirection].toString() + " --------");

    }

    public void commitMove() {
        commitMove(intendedDirection);
    }


    private void changePosition(int index, int operation) {
        int[] temp = currentPosition.clone();

        if (operation == 0) {
            temp[index] -= 1;
        }

        if (operation == 1) {
            temp[index] += 1;
        }


        if (!checkBackwards(temp)) {
            lastPosition = currentPosition.clone();
            shadows.add(lastPosition);
            currentPosition = temp;
            direction = intendedDirection;

        }else{
            //TODO hier muss auf die naechste position ein schatten gesetzt werden
            intendedDirection = direction;


            this.commitMove(direction);
        }



    }


    private boolean checkBackwards(int[] newPosition) {
        if (lastPosition.length == 0) {
            return false;
        }
        return Arrays.equals(newPosition, lastPosition);
    }

    public static void resetNextId(){
        nextId.set(1);
    }

}
