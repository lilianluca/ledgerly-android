package com.lilianluca.ledgerly.data.repository

import com.lilianluca.ledgerly.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Keeps transactions in memory only; data is lost when the app process ends. Replaced by Room in milestone 3. */
class InMemoryTransactionRepository(
    initialTransactions: List<Transaction> = emptyList(),
) : TransactionRepository {

    private val transactions = MutableStateFlow(initialTransactions.sortedWith(newestFirst))

    // Expose a read-only view so nobody outside can modify the state directly
    override fun observeTransactions(): Flow<List<Transaction>> = transactions.asStateFlow()

    override suspend fun addTransaction(transaction: Transaction) {
        // update {} is atomic: safe even if called from several coroutines at once
        transactions.update { current ->
            val nextId = (current.maxOfOrNull { it.id } ?: 0L) + 1
            (current + transaction.copy(id = nextId)).sortedWith(newestFirst)
        }
    }

    private companion object {
        // Newer date first; for the same date, the most recently added first
        val newestFirst = compareByDescending<Transaction> { it.date }.thenByDescending { it.id }
    }
}
