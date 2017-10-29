package com.ticktacktock.tictactoe

import android.app.Application
import android.support.annotation.VisibleForTesting
import com.ticktacktock.tictactoe.di.AppComponent
import com.ticktacktock.tictactoe.di.DaggerAppComponent
import com.ticktacktock.tictactoe.di.TicTacToeModule

/**
 * Created by krishan on 25/10/17.
 */
open class BaseApp : Application() {

    lateinit var appComponent: AppComponent
        @VisibleForTesting set

    override fun onCreate() {
        super.onCreate()
        createComponent()
    }

    open fun createComponent() {
        appComponent = DaggerAppComponent.builder().ticTacToeModule(TicTacToeModule()).build()
    }

}