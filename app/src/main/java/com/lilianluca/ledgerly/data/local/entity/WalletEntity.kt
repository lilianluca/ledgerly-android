package com.lilianluca.ledgerly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletType

/** Database row for a wallet (table "wallets"). */
@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: WalletType,
    /** In minor units (haléře). */
    val initialBalance: Long,
    val isArchived: Boolean,
)

fun WalletEntity.toDomain(): Wallet = Wallet(
    id = id,
    name = name,
    type = type,
    initialBalance = initialBalance,
    isArchived = isArchived,
)

fun Wallet.toEntity(): WalletEntity = WalletEntity(
    id = id,
    name = name,
    type = type,
    initialBalance = initialBalance,
    isArchived = isArchived,
)
