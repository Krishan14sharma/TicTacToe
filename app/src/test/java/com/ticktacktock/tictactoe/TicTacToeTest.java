package com.ticktacktock.tictactoe;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by krishan on 19/10/17.
 */

public class TicTacToeTest {

    private TicTacToe ticTacToe; // under test
    @Mock
    TicTacToe.TicTacToeListener ticTacToeListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ticTacToe = new TicTacToe(ticTacToeListener);
    }

    @Test
    public void testBoardIsEmptyInitiallyAndAllMovesAreValid() {
        for (int i = 0; i < TicTacToe.BOARD_ROW; i++) {
            for (int j = 0; j < TicTacToe.BOARD_COLUMN; j++) {
                assertThat(ticTacToe.isValidMove(i, j)).isTrue();
            }
        }
    }

    @Test
    public void testAlreadyMovedPositionIsNotValid() {
        assertThat(ticTacToe.moveAt(1, 1)).isTrue();
        assertThat(ticTacToe.moveAt(1, 1)).isFalse();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testThrowsErrorOnWrongCoordinates() {
        ticTacToe.moveAt(0, 3);
    }

    @Test
    public void testPlayerMovesAreRecordedCorrectlyOnBoard() {
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        assertThat(ticTacToe.getMoveAt(0, 0)).isEqualTo(TicTacToe.BoardState.SPACE);
        ticTacToe.moveAt(0, 0);
        assertThat(ticTacToe.getMoveAt(0, 0)).isEqualTo(playerToMove.move);
        ticTacToe.moveAt(0, 2);
        assertThat(ticTacToe.getMoveAt(0, 2)).isNotEqualTo(playerToMove.move).isNotEqualTo(TicTacToe.BoardState.SPACE);
        ticTacToe.moveAt(0, 1);
        assertThat(ticTacToe.getMoveAt(0, 1)).isEqualTo(playerToMove.move);
    }

    @Test
    public void testPlayerTurnChangeAfterAMove() {
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        assertThat(ticTacToe.moveAt(0, 0)).isTrue();
        assertThat(ticTacToe.getPlayerToMove()).isNotEqualTo(playerToMove);
    }

    @Test
    public void testNotValidMoveByPlayer_PlayerRemainsSame() {
        assertThat(ticTacToe.moveAt(0, 0)).isTrue();
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        assertThat(ticTacToe.moveAt(0, 0)).isFalse();
        assertThat(ticTacToe.getPlayerToMove()).isEqualTo(playerToMove);
    }

    @Test
    public void testListenerCalledWhenPlayerAWins() {
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(1, 1); // player 2 moves
        ticTacToe.moveAt(0, 2);// player 1 moves
        ticTacToe.moveAt(1, 2);// player 2 moves
        ticTacToe.moveAt(0, 1);//player 1 moves
        verify(ticTacToeListener).gameWonBy(playerToMove);
        testBoardIsEmptyInitiallyAndAllMovesAreValid(); // test game is reset
    }

    @Test
    public void testListenerCalledWhenPlayerBWins() {
        ticTacToe.moveAt(2, 2); // player 1 moves
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 2 moves
        ticTacToe.moveAt(1, 1); // player 1 moves
        ticTacToe.moveAt(0, 2);// player 2 moves
        ticTacToe.moveAt(1, 2);// player 1 moves
        ticTacToe.moveAt(0, 1);//player 2 moves
        verify(ticTacToeListener).gameWonBy(playerToMove);
        testBoardIsEmptyInitiallyAndAllMovesAreValid(); // test game is reset
    }


    @Test
    public void testNoListenerCalledThereIsNoWin() {
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(1, 1); // player 2 moves
        ticTacToe.moveAt(0, 2);// player 1 moves
        ticTacToe.moveAt(1, 2);// player 2 moves
        ticTacToe.moveAt(2, 1);//player 1 moves
        verify(ticTacToeListener, never()).gameWonBy(playerToMove);
        verify(ticTacToeListener, never()).gameEndsWithATie();
    }

    /**
     * _____
     * |x o x|
     * |o o x|
     * |x x o|
     */
    @Test
    public void testListenerCalledThereIsATie() {
        TicTacToeTestHelper.makeAGameTie(ticTacToe);
        verify(ticTacToeListener).gameEndsWithATie();
        verify(ticTacToeListener, never()).gameWonBy((TicTacToe.BoardPlayer) any());
        testBoardIsEmptyInitiallyAndAllMovesAreValid(); // test game is reset
    }

    @Test
    public void testOnResetGameBeginsAgain() {
        ticTacToe.moveAt(0, 0);
        ticTacToe.moveAt(0, 2);
        ticTacToe.resetGame();
        testBoardIsEmptyInitiallyAndAllMovesAreValid();
    }

}
