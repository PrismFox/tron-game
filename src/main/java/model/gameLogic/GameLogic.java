package model.gameLogic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.board.Board;
import model.config.IConfig;
import model.lobby.Lobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Data
public class GameLogic implements IGameLogic {

    private Board board;

    private IConfig config;

    private PlayerLogic playerLogic;

    private Lobby lobby;


    @Override
    public void startGame() {
        System.out.println("test");
    }

    @Override
    public void updateTick() {
        System.out.println("test");
    }

    @Override
    public List<int[]> checkCollision() {
        return null;
    }

    @Override
    public List<int[]> calculateStartPositions() {
        return null;
    }

    @Override
    public void endGame() {
        System.out.println("test");
    }

    @Override
    public int[] getWinnerStatus() {
        return new int[1];
    }

    private void init(){
        playerLogic = new PlayerLogic();
    }

}
