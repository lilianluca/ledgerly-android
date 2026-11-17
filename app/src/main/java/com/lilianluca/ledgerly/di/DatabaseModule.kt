package com.lilianluca.ledgerly.di

import android.content.Context
import androidx.room.Room
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.data.local.DefaultWalletCallback
import com.lilianluca.ledgerly.data.local.LedgerlyDatabase
import com.lilianluca.ledgerly.data.local.Migration1To2
import com.lilianluca.ledgerly.data.local.dao.TransactionDao
import com.lilianluca.ledgerly.data.local.dao.WalletDao
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
    fun provideDatabase(@ApplicationContext context: Context): LedgerlyDatabase {
        // Localized once, at the moment the default wallet is created
        val defaultWalletName = context.getString(R.string.default_wallet_name)
        return Room.databaseBuilder(
            context,
            LedgerlyDatabase::class.java,
            LedgerlyDatabase.NAME,
        )
            .addMigrations(Migration1To2(defaultWalletName))
            .addCallback(DefaultWalletCallback(defaultWalletName))
            .build()
    }

    @Provides
    fun provideTransactionDao(database: LedgerlyDatabase): TransactionDao =
        database.transactionDao()

    @Provides
    fun provideWalletDao(database: LedgerlyDatabase): WalletDao =
        database.walletDao()
}
