package com.ticktacktock.tictactoe.di

import com.ticktacktock.tictactoe.TicTacToeFunctionalTest
import com.ticktacktock.tictactoe.TicTacToe
import dagger.Component
import org.mockito.Mockito
import javax.inject.Singleton

/**
 * Created by krishan on 25/10/17.
 */

@Component(modules = (arrayOf(TicTacToeModule::class)))
@Singleton
interface TestAppComponent : AppComponent {
    fun inject(test: TicTacToeFunctionalTest)
}

class TicTacToeTestModule : TicTacToeModule() {

    override fun providesTicTacToe(): TicTacToe {
        return Mockito.mock(TicTacToe::class.java)
    }
}
