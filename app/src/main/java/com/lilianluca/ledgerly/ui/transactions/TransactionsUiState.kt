package com.lilianluca.ledgerly.ui.transactions

import com.lilianluca.ledgerly.domain.model.Transaction

/** Everything the transactions screen needs to render, in one immutable object. */
data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
)
