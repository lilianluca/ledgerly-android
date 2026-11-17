package com.lilianluca.ledgerly.data.local.entity

import com.lilianluca.ledgerly.domain.model.Transaction

// Mapping between the database row and the domain model keeps Room annotations out of the UI

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    type = type,
    amount = amount,
    category = category,
    date = date,
    note = note,
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = type,
    amount = amount,
    category = category,
    date = date,
    note = note,
)
