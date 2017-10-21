package com.ticktacktock.tictactoe;

/**
 * Created by krishan on 20/10/17.
 */

public class TicTacToeTestHelper {

    static void makeAGameTie(TicTacToe ticTacToe) {
        TicTacToe.BoardPlayer playerToMove = ticTacToe.getPlayerToMove();
        ticTacToe.moveAt(0, 0); // player 1 moves
        ticTacToe.moveAt(1, 1); // player 2 moves
        ticTacToe.moveAt(2, 0);// player 1 moves
        ticTacToe.moveAt(1, 0);// player 2 moves
        ticTacToe.moveAt(1, 2);//player 1 moves
        ticTacToe.moveAt(0, 1);
        ticTacToe.moveAt(2, 1);
        ticTacToe.moveAt(2, 2);
        ticTacToe.moveAt(0, 2);
    }

}
