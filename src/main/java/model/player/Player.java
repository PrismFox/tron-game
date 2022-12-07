package model.player;

import Enums.Direction;
import lombok.Data;

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
    private Direction intendedDirection;
    private List<String> mapping;
    private int color;

    public Player(int color){
     this.id = nextId.getAndIncrement();
     this.color = color;
    }

}
