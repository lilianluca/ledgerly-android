package com.lilianluca.ledgerly.data.local.entity

import androidx.room.Embedded
import com.lilianluca.ledgerly.domain.model.WalletWithBalance

/** Query result: all wallet columns (embedded) plus the computed `balance` column. */
data class WalletWithBalanceRow(
    @Embedded val wallet: WalletEntity,
    val balance: Long,
)

fun WalletWithBalanceRow.toDomain(): WalletWithBalance = WalletWithBalance(
    wallet = wallet.toDomain(),
    balance = balance,
)
