package com.lilianluca.ledgerly.di

import com.lilianluca.ledgerly.data.repository.InMemoryTransactionRepository
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import com.lilianluca.ledgerly.data.sample.sampleTransactions

/**
 * Manual dependency container: creates app-wide singletons once.
 * Will be replaced by Hilt in milestone 4.
 */
class AppContainer {
    val transactionRepository: TransactionRepository =
        InMemoryTransactionRepository(initialTransactions = sampleTransactions)
}
