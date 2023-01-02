package edu.cads.bai5.vsp.tron.model;

import de.haw.vsp.tron.model.board.IBoard;
import de.haw.vsp.tron.model.gamelogic.IGameLogic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class GameLogicTest {

    @Mock
    IGameLogic gameLogic;

    @Mock
    IBoard board;



@Test
public void calculateStartPositionsTest(){
    Mockito.when(board.getObstacles()).thenReturn(new ArrayList<>());
    Mockito.when(board.getBoardSize()).thenReturn(new int[]{16,16});
   /* Mockito.when(gameLogic.getPlayerManager().getLivingPlayers()).thenReturn(new ArrayList<>
                (Arrays.asList(new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.BLUE),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.GREEN),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.GREEN),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.RED) )));*/
    gameLogic.calculateStartPositions();


}


}