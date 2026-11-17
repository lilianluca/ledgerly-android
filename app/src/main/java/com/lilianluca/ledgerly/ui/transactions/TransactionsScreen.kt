package com.lilianluca.ledgerly.ui.transactions

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme

/** Screen showing all transactions. In milestone 2 it will get its data from a ViewModel. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.transactions_title)) })
        },
    ) { innerPadding ->
        TransactionList(
            transactions = transactions,
            modifier = Modifier.fillMaxSize(),
            // Padding goes inside the list, so content can scroll behind system bars
            contentPadding = innerPadding,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TransactionsScreenPreview() {
    LedgerlyTheme {
        TransactionsScreen(transactions = sampleTransactions)
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TransactionsScreenDarkPreview() {
    LedgerlyTheme {
        TransactionsScreen(transactions = sampleTransactions)
    }
}
