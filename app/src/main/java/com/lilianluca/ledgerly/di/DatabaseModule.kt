package com.lilianluca.ledgerly.di

import android.content.Context
import androidx.room.Room
import com.lilianluca.ledgerly.data.local.LedgerlyDatabase
import com.lilianluca.ledgerly.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Recipes for database objects, which can't be created via an @Inject constructor. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // One database instance for the whole app; opening it is expensive
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LedgerlyDatabase =
        Room.databaseBuilder(
            context,
            LedgerlyDatabase::class.java,
            LedgerlyDatabase.NAME,
        ).build()

    @Provides
    fun provideTransactionDao(database: LedgerlyDatabase): TransactionDao =
        database.transactionDao()
}
