package model.player;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class Player implements IPlayer {

    private int id;
    private boolean aliveStatus;
    private List<String> mapping;
    private int color;
    private int[] currentPosition;


}
