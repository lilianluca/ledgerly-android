package com.lilianluca.ledgerly.domain.model

import java.time.LocalDate

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

data class Transaction(
    val id: Long,
    val type: TransactionType,
    /** Amount in minor units, always positive. */
    val amount: Long,
    /** Wallet the money comes from (expense, transfer) or goes to (income). */
    val walletId: Long,
    /** Destination wallet; only for transfers, otherwise null. */
    val toWalletId: Long? = null,
    /** Category name, null for transfers. */
    val category: String?,
    val date: LocalDate,
    val note: String? = null
)
