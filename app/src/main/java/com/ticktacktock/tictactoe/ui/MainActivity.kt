package com.ticktacktock.tictactoe.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ticktacktock.tictactoe.R
import com.ticktacktock.tictactoe.TicTacToe
import com.ticktacktock.tictactoe.customView.TicTacToeView
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), TicTacToe.TicTacToeListener, TicTacToeView.SquarePressedListener {
    lateinit var ticTacToe: TicTacToe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        ticTacToe = TicTacToe()
        ticTacToe.setTicTacToeListener(this)
        ticTacToeView.squarePressListener = this

        resetButton.setOnClickListener {
            ticTacToe.resetGame()
            resetGameUi()
            resetButton.visibility = View.GONE
        }
    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }

    override fun movedAt(x: Int, y: Int, z: Int) {
        if (z == TicTacToe.BoardState.MOVE_X)
            ticTacToeView.drawXAtPosition(x, y)
        else
            ticTacToeView.drawOAtPosition(x, y)
    }

    override fun gameEndsWithATie() {
        information.visibility = View.VISIBLE
        information.text = getString(R.string.game_ends_draw)
        resetButton.visibility = View.VISIBLE
        ticTacToeView.isEnabled = false
    }

    private fun resetGameUi() {
        ticTacToeView.reset()
        ticTacToeView.isEnabled = true
        information.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    override fun gameWonBy(boardPlayer: TicTacToe.BoardPlayer, winCoords: Array<TicTacToe.SquareCoordinates>) {
        information.visibility = View.VISIBLE
        information.text = "Game won by ${if (boardPlayer.move == TicTacToe.BoardState.MOVE_X) "X" else "O"}"
        ticTacToeView.animateWin(winCoords)
        ticTacToeView.isEnabled = false
        resetButton.visibility = View.VISIBLE
    }

}
