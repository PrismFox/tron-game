package model.player;

import Enums.Direction;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Player {

    private static final AtomicInteger nextId = new AtomicInteger();
    private int id;
    private int[] currentPosition = new int[2];

    private int[] lastPosition = new int[2];
    private List<int[]> shadows;
    private boolean alive = true;
    private int direction; //Actual direction
    private int intendedDirection; //Can change multiple times during a tick
    private List<String> mapping;
    private int color;

    public Player(int color) {
        this.id = nextId.getAndIncrement();
        this.color = color;
    }


    public void addShadow() {
        return;
    }

    public void commitMove() {
        if (Direction.UP == Direction.values()[intendedDirection]) {
            int[] temp = currentPosition.clone();
            temp[1] -= 1;
            if (!checkBackwards(temp)) {
                lastPosition = currentPosition.clone();
                shadows.add(lastPosition);
                currentPosition = temp;

                direction = intendedDirection;
            }
            intendedDirection = direction;
            commitMove();
        }

        if (Direction.Down == Direction.values()[intendedDirection]) {
            int[] temp = currentPosition.clone();
            temp[1] += 1;
            if (!checkBackwards(temp)) {
                lastPosition = currentPosition.clone();
                shadows.add(lastPosition);
                currentPosition = temp;

                direction = intendedDirection;
            }

            intendedDirection = direction;
            commitMove();

        }

        if (Direction.LEFT == Direction.values()[intendedDirection]) {
            int[] temp = currentPosition.clone();
            temp[0] -= 1;
            if (!checkBackwards(temp)) {
                lastPosition = currentPosition.clone();
                shadows.add(lastPosition);
                currentPosition = temp;
                direction = intendedDirection;
            }

            intendedDirection = direction;
            commitMove();

        }

        if (Direction.RIGHT == Direction.values()[intendedDirection]) {
            int[] temp = currentPosition.clone();
            temp[0] += 1;
            if (!checkBackwards(temp)) {
                lastPosition = currentPosition.clone();
                shadows.add(lastPosition);
                currentPosition = temp;

                direction = intendedDirection;
            }

            intendedDirection = direction;
            commitMove();
        }

    }

    private boolean checkBackwards(int[] newPosition) {
        return Arrays.equals(newPosition, lastPosition);
    }

}
