package com.lilianluca.ledgerly

import android.app.Application
import com.lilianluca.ledgerly.di.AppContainer

/** Lives for the whole app process; holds the dependency container shared by all screens. */
class LedgerlyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(context = this)
    }
}
