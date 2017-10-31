package com.ticktacktock.tictactoe.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
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
        Toast.makeText(this, "Game Ends with a tie", Toast.LENGTH_SHORT).show()
        resetButton.visibility = View.VISIBLE
        ticTacToeView.isEnabled = false
    }

    private fun resetGameUi() {
        ticTacToeView.reset()
        ticTacToeView.isEnabled = true
    }

    override fun gameWonBy(boardPlayer: TicTacToe.BoardPlayer, winCoords: Array<TicTacToe.SquareCoordinates>) {
        Toast.makeText(this, "Game won by" + boardPlayer.move, Toast.LENGTH_SHORT).show()
        ticTacToeView.animateWin(winCoords)
        ticTacToeView.isEnabled = false
        resetButton.visibility = View.VISIBLE
//        ticTacToe.resetGame()
//        resetGameUi()
    }

}
