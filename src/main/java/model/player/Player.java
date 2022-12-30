package model.player;

import Enums.Color;
import Enums.Direction;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Player {

    private static final AtomicInteger nextId = new AtomicInteger();
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


    public void commitMove() {
        if (Direction.UP == Direction.values()[intendedDirection]) {
            this.changePosition(1, 0);
        }

        if (Direction.Down == Direction.values()[intendedDirection]) {
            this.changePosition(1, 1);
        }

        if (Direction.LEFT == Direction.values()[intendedDirection]) {
            this.changePosition(0, 0);
        }

        if (Direction.RIGHT == Direction.values()[intendedDirection]) {
            this.changePosition(0, 1);
        }

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
        }

        intendedDirection = direction;
        this.commitMove();
    }


    private boolean checkBackwards(int[] newPosition) {
        if (lastPosition.length == 0) {
            return false;
        }
        return Arrays.equals(newPosition, lastPosition);
    }


}
