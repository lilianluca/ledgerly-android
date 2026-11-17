package com.lilianluca.ledgerly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lilianluca.ledgerly.data.local.dao.TransactionDao
import com.lilianluca.ledgerly.data.local.entity.TransactionEntity

/**
 * The app's SQLite database. Room generates the implementation at build time.
 *
 * Bump `version` whenever tables change and provide a migration; the exported schema
 * for each version lives in app/schemas/.
 */
@Database(
    entities = [TransactionEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class LedgerlyDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        const val NAME = "ledgerly.db"
    }
}
