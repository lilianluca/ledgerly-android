package com.lilianluca.ledgerly.data.repository

import com.lilianluca.ledgerly.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

/** Single source of truth for transactions. UI and ViewModels depend only on this interface. */
interface TransactionRepository {

    /** Emits the current list of transactions (newest first) and every subsequent change. */
    fun observeTransactions(): Flow<List<Transaction>>

    /** Adds a new transaction. The passed [Transaction.id] is ignored; the repository assigns one. */
    suspend fun addTransaction(transaction: Transaction)
}
