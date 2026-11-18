package com.lilianluca.ledgerly.ui.transactions

import com.lilianluca.ledgerly.domain.model.Transaction

/** Everything the transactions screen needs to render, in one immutable object. */
data class TransactionsUiState(
    val transactions: List<TransactionListItem> = emptyList(),
    val isLoading: Boolean = true,
)

/** A transaction together with the wallet names shown in its row. */
data class TransactionListItem(
    val transaction: Transaction,
    val walletName: String?,
    /** Destination wallet name; only for transfers. */
    val toWalletName: String? = null,
)
