package com.ticktacktock.tictactoe.di

import com.ticktacktock.tictactoe.TicTacToe
import dagger.Module
import dagger.Provides

/**
 * Created by krishan on 25/10/17.
 */
@Module
open class TicTacToeModule {

    @Provides
    open fun providesTicTacToe(): TicTacToe {
        return TicTacToe()
    }

}
