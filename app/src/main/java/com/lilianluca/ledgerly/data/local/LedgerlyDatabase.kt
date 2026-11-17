package com.lilianluca.ledgerly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lilianluca.ledgerly.data.local.dao.TransactionDao
import com.lilianluca.ledgerly.data.local.dao.WalletDao
import com.lilianluca.ledgerly.data.local.entity.TransactionEntity
import com.lilianluca.ledgerly.data.local.entity.WalletEntity

/**
 * The app's SQLite database. Room generates the implementation at build time.
 *
 * Bump `version` whenever tables change and provide a migration; the exported schema
 * for each version lives in app/schemas/.
 *
 * History:
 * - 1: transactions
 * - 2: wallets; transactions reference a wallet (see Migration1To2)
 */
@Database(
    entities = [TransactionEntity::class, WalletEntity::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class LedgerlyDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun walletDao(): WalletDao

    companion object {
        const val NAME = "ledgerly.db"
    }
}
