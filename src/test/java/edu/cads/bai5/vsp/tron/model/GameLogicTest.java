package edu.cads.bai5.vsp.tron.model;

import Enums.Color;
import model.board.IBoard;
import model.gamelogic.IGameLogic;
import model.player.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

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
    Mockito.when(gameLogic.getPlayerLogic().getPlayerManager().getLivingPlayers()).thenReturn(new ArrayList<>
                (Arrays.asList(new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.BLUE),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.GREEN),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.GREEN),
                        new Player(new ArrayList<>(Arrays.asList("test", "test")), Color.RED) )));
    gameLogic.calculateStartPositions();


}


}