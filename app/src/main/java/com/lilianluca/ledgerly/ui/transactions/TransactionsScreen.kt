package com.lilianluca.ledgerly.ui.transactions

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.data.sample.sampleTransactions
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme

/** Stateful entry point: obtains the ViewModel and observes its state. */
@Composable
fun TransactionsRoute(
    onAddTransactionClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionsViewModel = hiltViewModel(),
) {
    // Collects only while the screen is at least STARTED (visible), saving resources in background
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsScreen(
        uiState = uiState,
        onAddTransactionClick = onAddTransactionClick,
        modifier = modifier,
    )
}

/** Stateless screen: renders the given state only, so it is easy to preview and test. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    uiState: TransactionsUiState,
    onAddTransactionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.transactions_title)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_transaction),
                )
            }
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> CenteredBox(Modifier.padding(innerPadding)) {
                CircularProgressIndicator()
            }

            uiState.transactions.isEmpty() -> CenteredBox(Modifier.padding(innerPadding)) {
                Text(
                    text = stringResource(R.string.transactions_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            else -> TransactionList(
                transactions = uiState.transactions,
                modifier = Modifier.fillMaxSize(),
                // Padding goes inside the list, so content can scroll behind system bars
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun CenteredBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TransactionsScreenPreview() {
    LedgerlyTheme {
        TransactionsScreen(
            uiState = TransactionsUiState(sampleTransactions, isLoading = false),
            onAddTransactionClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TransactionsScreenDarkPreview() {
    LedgerlyTheme {
        TransactionsScreen(
            uiState = TransactionsUiState(sampleTransactions, isLoading = false),
            onAddTransactionClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TransactionsScreenEmptyPreview() {
    LedgerlyTheme {
        TransactionsScreen(
            uiState = TransactionsUiState(emptyList(), isLoading = false),
            onAddTransactionClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TransactionsScreenLoadingPreview() {
    LedgerlyTheme {
        TransactionsScreen(
            uiState = TransactionsUiState(isLoading = true),
            onAddTransactionClick = {},
        )
    }
}
