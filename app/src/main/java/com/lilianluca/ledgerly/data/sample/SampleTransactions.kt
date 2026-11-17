package com.lilianluca.ledgerly.data.sample

import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.domain.model.TransactionType
import java.time.LocalDate

// Fake data for previews only; the real app starts with an empty database

private const val CASH_ID = 1L
private const val BANK_ID = 2L

val sampleTransactions = listOf(
    Transaction(
        id = 1,
        type = TransactionType.INCOME,
        amount = 1_800_000,
        walletId = BANK_ID,
        category = "Brigáda",
        date = LocalDate.of(2026, 11, 16),
    ),
    Transaction(
        id = 2,
        type = TransactionType.EXPENSE,
        amount = 34_950,
        walletId = CASH_ID,
        category = "Potraviny",
        date = LocalDate.of(2026, 11, 15),
        note = "Lidl",
    ),
    Transaction(
        id = 3,
        type = TransactionType.TRANSFER,
        amount = 200_000,
        walletId = BANK_ID,
        toWalletId = CASH_ID,
        category = null,
        date = LocalDate.of(2026, 11, 14),
        note = "Výběr z bankomatu",
    ),
    Transaction(
        id = 4,
        type = TransactionType.EXPENSE,
        amount = 18_900,
        walletId = CASH_ID,
        category = "Restaurace",
        date = LocalDate.of(2026, 11, 12),
    ),
    Transaction(
        id = 5,
        type = TransactionType.EXPENSE,
        amount = 1_250_000,
        walletId = BANK_ID,
        category = "Bydlení",
        date = LocalDate.of(2026, 11, 1),
        note = "Nájem za listopad",
    ),
    Transaction(
        id = 6,
        type = TransactionType.INCOME,
        amount = 3_500_000,
        walletId = BANK_ID,
        category = "Výplata",
        date = LocalDate.of(2026, 10, 31),
    ),
)
