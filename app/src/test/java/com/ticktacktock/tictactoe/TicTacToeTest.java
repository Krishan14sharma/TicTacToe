package com.ticktacktock.tictactoe;

import com.ticktacktock.tictactoe.TicTacToe.BoardPlayer;

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
        ticTacToe = new TicTacToe();
        ticTacToe.setTicTacToeListener(ticTacToeListener);
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
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
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
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        assertThat(ticTacToe.moveAt(0, 0)).isTrue();
        assertThat(ticTacToe.getPlayerToMove()).isNotEqualTo(playerToMove);
    }

    @Test
    public void testNotValidMoveByPlayer_PlayerRemainsSame() {
        assertThat(ticTacToe.moveAt(0, 0)).isTrue();
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        assertThat(ticTacToe.moveAt(0, 0)).isFalse();
        assertThat(ticTacToe.getPlayerToMove()).isEqualTo(playerToMove);
    }

    @Test
    public void testPlayerWinsByADiagonalMove() {
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(0, 1); // player 2 moves
        ticTacToe.moveAt(1, 1);// player 1 moves
        ticTacToe.moveAt(0, 2);// player 2 moves
        ticTacToe.moveAt(2, 2);//player 1 moves
        verify(ticTacToeListener).gameWonBy(playerToMove, new TicTacToe.SquareCoordinates[] {
                new TicTacToe.SquareCoordinates(0, 0),
                new TicTacToe.SquareCoordinates(1, 1),
                new TicTacToe.SquareCoordinates(2, 2),
        });
    }

    @Test
    public void testListenerCalledWhenPlayerAWins() {
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(1, 1); // player 2 moves
        ticTacToe.moveAt(0, 2);// player 1 moves
        ticTacToe.moveAt(1, 2);// player 2 moves
        ticTacToe.moveAt(0, 1);//player 1 moves
        verify(ticTacToeListener).gameWonBy(playerToMove, new TicTacToe.SquareCoordinates[] {
                new TicTacToe.SquareCoordinates(0, 0),
                new TicTacToe.SquareCoordinates(0, 1),
                new TicTacToe.SquareCoordinates(0, 2),
        });
    }

    @Test
    public void testListenerCalledWhenPlayerBWins() {
        ticTacToe.moveAt(2, 2); // player 1 moves
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 2 moves
        ticTacToe.moveAt(1, 1); // player 1 moves
        ticTacToe.moveAt(0, 2);// player 2 moves
        ticTacToe.moveAt(1, 2);// player 1 moves
        ticTacToe.moveAt(0, 1);//player 2 moves
        // todo fix this need to be in order
        verify(ticTacToeListener).gameWonBy(playerToMove, new TicTacToe.SquareCoordinates[] {
                new TicTacToe.SquareCoordinates(0, 0),
                new TicTacToe.SquareCoordinates(0, 1),
                new TicTacToe.SquareCoordinates(0, 2),
        });
    }


    @Test
    public void testNoListenerCalledThereIsNoWin() {
        BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(1, 1); // player 2 moves
        ticTacToe.moveAt(0, 2);// player 1 moves
        ticTacToe.moveAt(1, 2);// player 2 moves
        ticTacToe.moveAt(2, 1);//player 1 moves
        verify(ticTacToeListener, never()).gameWonBy(any(BoardPlayer.class), any(TicTacToe.SquareCoordinates[].class));
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
        verify(ticTacToeListener, never()).gameWonBy((BoardPlayer) any(), any(TicTacToe.SquareCoordinates[].class));
    }

    @Test
    public void testOnResetGameBeginsAgain() {
        ticTacToe.moveAt(0, 0);
        ticTacToe.moveAt(0, 2);
        ticTacToe.resetGame();
    }

}
