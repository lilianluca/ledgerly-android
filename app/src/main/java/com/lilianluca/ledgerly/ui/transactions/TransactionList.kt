package com.lilianluca.ledgerly.ui.transactions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme

/** Stateless, scrollable list of transactions. Only visible rows are composed. */
@Composable
fun TransactionList(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        // Stable keys let Compose track rows across list changes (like React's `key`)
        items(transactions, key = { it.id }) { transaction ->
            TransactionItem(transaction)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionListPreview() {
    LedgerlyTheme {
        TransactionList(transactions = sampleTransactions)
    }
}
