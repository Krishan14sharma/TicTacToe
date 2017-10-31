package com.ticktacktock.tictactoe.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ticktacktock.tictactoe.R
import com.ticktacktock.tictactoe.TicTacToe
import com.ticktacktock.tictactoe.customView.TicTacToeView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), TicTacToe.TicTacToeListener, TicTacToeView.SquarePressedListener {
    lateinit var ticTacToe: TicTacToe

//    lateinit var buttons: Array<Array<Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        ticTacToe = TicTacToe()
        ticTacToe.setTicTacToeListener(this)
        ticTacToeView.squarePressListener = this
        setSupportActionBar(toolbar)
//        val row1 = arrayOf(button_0_0, button_0_1, button_0_2)
//        val row2 = arrayOf(button_1_0, button_1_1, button_1_2)
//        val row3 = arrayOf(button_2_0, button_2_1, button_2_2)
//        buttons = arrayOf(row1, row2, row3)
    }

//    fun ticTacToeClick(view: View) {
//        buttons.forEachIndexed({ i, buttons ->
//            buttons.forEachIndexed({ j, button ->
//                if ((button === view)) {
//                    ticTacToe.moveAt(i, j)
//                }
//            })
//        })
//    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }

    override fun movedAt(x: Int, y: Int, z: Int) {
//        buttons[x][y].text = if (z == MOVE_X) "X" else "O"
        if (z == TicTacToe.BoardState.MOVE_X)
            ticTacToeView.drawXAtPosition(x, y)
        else
            ticTacToeView.drawOAtPosition(x, y)
    }

    override fun gameEndsWithATie() {
        Toast.makeText(this, "Game Ends with a tie", Toast.LENGTH_SHORT).show()
        ticTacToe.resetGame()
        resetGameUi()
    }

    private fun resetGameUi() {
//        buttons.forEach {
//            it.forEach { button -> button.text = "" }
//        }
        ticTacToeView.reset()
    }

    override fun gameWonBy(boardPlayer: TicTacToe.BoardPlayer) {
        Toast.makeText(this, "Game won by" + boardPlayer.move, Toast.LENGTH_SHORT).show()
        ticTacToe.resetGame()
        resetGameUi()
    }

}
