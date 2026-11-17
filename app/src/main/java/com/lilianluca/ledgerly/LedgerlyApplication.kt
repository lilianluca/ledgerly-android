package com.lilianluca.ledgerly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Application entry point; Hilt creates the app-wide dependency container here. */
@HiltAndroidApp
class LedgerlyApplication : Application()
