package com.lilianluca.ledgerly.di

import android.content.Context
import androidx.room.Room
import com.lilianluca.ledgerly.data.local.LedgerlyDatabase
import com.lilianluca.ledgerly.data.repository.RoomTransactionRepository
import com.lilianluca.ledgerly.data.repository.TransactionRepository

/**
 * Manual dependency container: creates app-wide singletons once.
 * Will be replaced by Hilt in milestone 4.
 *
 * @param context application context (never an Activity, which would leak)
 */
class AppContainer(context: Context) {

    // One database instance for the whole app; opening it is expensive
    private val database: LedgerlyDatabase = Room.databaseBuilder(
        context,
        LedgerlyDatabase::class.java,
        LedgerlyDatabase.NAME,
    ).build()

    val transactionRepository: TransactionRepository =
        RoomTransactionRepository(database.transactionDao())
}
