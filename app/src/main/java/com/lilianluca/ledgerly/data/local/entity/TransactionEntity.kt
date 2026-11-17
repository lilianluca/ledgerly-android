package com.lilianluca.ledgerly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
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
    foreignKeys = [
        // RESTRICT: a wallet that has transactions can't be deleted, only archived
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["toWalletId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        // Lists are sorted and statistics filtered by date
        Index("date"),
        // Foreign key columns should be indexed, otherwise joins and parent deletes scan the whole table
        Index("walletId"),
        Index("toWalletId"),
    ],
)
data class TransactionEntity(
    // 0 means "not inserted yet"; SQLite assigns the real id on insert
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    /** Amount in minor units (haléře), always positive. */
    val amount: Long,
    val walletId: Long,
    /** Destination wallet for transfers, otherwise null. */
    val toWalletId: Long?,
    /** Null for transfers. */
    val category: String?,
    val date: LocalDate,
    val note: String?,
)
