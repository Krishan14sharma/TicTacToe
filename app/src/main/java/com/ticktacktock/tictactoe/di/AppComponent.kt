package com.ticktacktock.tictactoe.di

import com.ticktacktock.tictactoe.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by krishan on 25/10/17.
 */
@Singleton
@Component(modules = (arrayOf(TicTacToeModule::class)))
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}