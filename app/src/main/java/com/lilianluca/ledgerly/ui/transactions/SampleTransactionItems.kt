package com.lilianluca.ledgerly.ui.transactions

import com.lilianluca.ledgerly.data.sample.sampleTransactions

private val sampleWalletNames = mapOf(1L to "Hotovost", 2L to "Fio")

/** Fake list rows for previews. */
internal val sampleTransactionItems = sampleTransactions.map { transaction ->
    TransactionListItem(
        transaction = transaction,
        walletName = sampleWalletNames[transaction.walletId],
        toWalletName = transaction.toWalletId?.let { sampleWalletNames[it] },
    )
}
