package com.lilianluca.ledgerly.data.repository

import com.lilianluca.ledgerly.data.local.dao.TransactionDao
import com.lilianluca.ledgerly.data.local.entity.toDomain
import com.lilianluca.ledgerly.data.local.entity.toEntity
import com.lilianluca.ledgerly.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Stores transactions in the Room database, so they survive app restarts. */
class RoomTransactionRepository(
    private val transactionDao: TransactionDao,
) : TransactionRepository {

    override fun observeTransactions(): Flow<List<Transaction>> =
        // Outer map transforms each emission of the stream, inner map each row of the list
        transactionDao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun addTransaction(transaction: Transaction) {
        // id = 0 lets SQLite assign a new id
        transactionDao.insert(transaction.copy(id = 0).toEntity())
    }
}
