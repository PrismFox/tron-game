package model.gameLogic;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.board.Board;
import model.config.IConfig;
import model.lobby.Lobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class GameLogicImpl implements GameLogic {

    private Board board;

    private IConfig config;

    private final PlayerLogic playerLogic;

    private Lobby lobby;


    @Override
    public void startGame() {

    }

    @Override
    public void updateTick() {

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

    }

    @Override
    public int[] getWinnerStatus() {
        getPlayerLogic().
    }
}
