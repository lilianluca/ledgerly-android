package com.lilianluca.ledgerly.data.sample

import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.domain.model.TransactionType
import java.time.LocalDate

// Fake data for development and previews, replaced by Room in milestone 3
val sampleTransactions = listOf(
    Transaction(
        id = 1,
        type = TransactionType.INCOME,
        amount = 1_800_000,
        category = "Brigáda",
        date = LocalDate.of(2026, 11, 16),
    ),
    Transaction(
        id = 2,
        type = TransactionType.EXPENSE,
        amount = 34_950,
        category = "Potraviny",
        date = LocalDate.of(2026, 11, 15),
        note = "Lidl",
    ),
    Transaction(
        id = 3,
        type = TransactionType.TRANSFER,
        amount = 200_000,
        category = null,
        date = LocalDate.of(2026, 11, 14),
        note = "Výběr z bankomatu",
    ),
    Transaction(
        id = 4,
        type = TransactionType.EXPENSE,
        amount = 18_900,
        category = "Restaurace",
        date = LocalDate.of(2026, 11, 12),
    ),
    Transaction(
        id = 5,
        type = TransactionType.EXPENSE,
        amount = 1_250_000,
        category = "Bydlení",
        date = LocalDate.of(2026, 11, 1),
        note = "Nájem za listopad",
    ),
    Transaction(
        id = 6,
        type = TransactionType.INCOME,
        amount = 3_500_000,
        category = "Výplata",
        date = LocalDate.of(2026, 10, 31),
    ),
)