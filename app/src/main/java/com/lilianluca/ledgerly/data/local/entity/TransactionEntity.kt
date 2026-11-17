package com.lilianluca.ledgerly.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lilianluca.ledgerly.domain.model.TransactionType
import java.time.LocalDate

/**
 * Database row for a transaction (table "transactions").
 *
 * Enums are stored by name as TEXT, so renaming a [TransactionType] constant requires a migration.
 */
@Entity(
    tableName = "transactions",
    // Lists are sorted and statistics filtered by date
    indices = [Index("date")],
)
data class TransactionEntity(
    // 0 means "not inserted yet"; SQLite assigns the real id on insert
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    /** Amount in minor units (haléře), always positive. */
    val amount: Long,
    /** Null for transfers. */
    val category: String?,
    val date: LocalDate,
    val note: String?,
)
