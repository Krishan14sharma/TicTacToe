package com.ticktacktock.tictactoe;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;

import java.lang.annotation.Retention;

import static com.ticktacktock.tictactoe.TicTacToe.BoardState.MOVE_O;
import static com.ticktacktock.tictactoe.TicTacToe.BoardState.MOVE_X;
import static com.ticktacktock.tictactoe.TicTacToe.BoardState.SPACE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by krishan on 19/10/17.
 * <p>
 * TicTacToe coordinates for each square
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * ------------------
 */

public class TicTacToe {

    public static final int BOARD_ROW = 3;
    public static final int BOARD_COLUMN = 3;
    private int board[][];

    private BoardPlayer playerToMove = BoardPlayer.PLAYER_X; // stores whose turn it is

    private TicTacToeListener ticTacToeListener;
    private int numberOfMoves = 0;

    public TicTacToe(TicTacToeListener ticTacToeListener) {
        if (ticTacToeListener == null) {
            throw new IllegalArgumentException("Listener can not be null");
        }
        initGame();
        this.ticTacToeListener = ticTacToeListener;
    }

    public boolean isValidMove(int x, int y) {
        return board[x][y] == SPACE;
    }

    public boolean moveAt(@IntRange (from = 0, to = 2) int x, @IntRange (from = 0, to = 2) int y) {
        if (x < 0 || x > BOARD_ROW - 1 || y < 0 || y > BOARD_COLUMN - 1) {
            throw new IllegalArgumentException(String.format("Coordinates %d and %d are not valid, valid set [0,1,2]", x, y));
        }
        if (!isValidMove(x, y)) {
            return false;
        }
        numberOfMoves++;
        ticTacToeListener.movedAt(x, y, playerToMove.move);
        board[x][y] = playerToMove.move;
        boolean hasWon = hasWon(x, y, playerToMove);
        if (hasWon) {
            ticTacToeListener.gameWonBy(playerToMove);
        }
        else if (numberOfMoves == BOARD_COLUMN * BOARD_ROW) {
            ticTacToeListener.gameEndsWithATie();
        }
        changeTurnToNextPlayer();
        return true;
    }

    private boolean hasWon(int x, int y, BoardPlayer playerToMove) {
        boolean hasWon = checkRow(x, playerToMove.move) || checkColumn(y, playerToMove.move);
        return hasWon;
    }

    private boolean checkColumn(int y, int movetoCheck) {
        for (int i = 0; i < BOARD_ROW; i++) {
            if (board[i][y] != movetoCheck) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRow(int x, int movetoCheck) {
        for (int i = 0; i < BOARD_ROW; i++) {
            if (board[x][i] != movetoCheck) {
                return false;
            }
        }
        return true;
    }

    private void changeTurnToNextPlayer() {
        if (playerToMove.equals(BoardPlayer.PLAYER_X)) {
            playerToMove = BoardPlayer.PLAYER_O;
        }
        else {
            playerToMove = BoardPlayer.PLAYER_X;
        }
    }

    public BoardPlayer getPlayerToMove() {
        return playerToMove;
    }

    private void initGame() {
        board = new int[BOARD_ROW][BOARD_COLUMN];
        playerToMove = BoardPlayer.PLAYER_X;
        numberOfMoves = 0;
    }

    public void resetGame() {
        initGame();
    }

    @BoardState
    public int getMoveAt(int x, int y) {
        if (board[x][y] == SPACE) {
            return SPACE;
        }
        else if (board[x][y] == MOVE_O) {
            return MOVE_O;
        }
        else {
            return MOVE_X;
        }
    }

    @Retention (SOURCE)
    @IntDef ({SPACE, MOVE_X, MOVE_O})
    public @interface BoardState {
        int SPACE = 0;
        int MOVE_X = 1;
        int MOVE_O = 2;
    }

    public enum BoardPlayer {
        PLAYER_X(BoardState.MOVE_X), PLAYER_O(BoardState.MOVE_O);
        int move = SPACE;

        BoardPlayer(int move) {
            this.move = move;
        }
    }

    public interface TicTacToeListener {
        void gameWonBy(BoardPlayer boardPlayer);

        void gameEndsWithATie();

        void movedAt(int x, int y, int move);
    }

}
