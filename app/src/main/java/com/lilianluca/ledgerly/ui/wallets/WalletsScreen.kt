package com.lilianluca.ledgerly.ui.wallets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletType
import com.lilianluca.ledgerly.domain.model.WalletWithBalance
import com.lilianluca.ledgerly.ui.components.withFabClearance
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme
import com.lilianluca.ledgerly.util.formatAmount

/** Stateful entry point: obtains the ViewModel and observes its state. */
@Composable
fun WalletsRoute(
    onAddWalletClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WalletsScreen(
        uiState = uiState,
        onAddWalletClick = onAddWalletClick,
        modifier = modifier,
    )
}

/** Stateless screen: total balance followed by the list of wallets. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletsScreen(
    uiState: WalletsUiState,
    onAddWalletClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.wallets_title)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWalletClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_wallet),
                )
            }
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding.withFabClearance(),
            ) {
                item {
                    TotalBalanceCard(
                        total = uiState.totalBalance,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                items(uiState.wallets, key = { it.wallet.id }) { item ->
                    WalletItem(item)
                }
            }
        }
    }
}

@Composable
private fun TotalBalanceCard(
    total: Long,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.wallets_total),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = formatAmount(total, locale),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun WalletItem(
    item: WalletWithBalance,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]
    ListItem(
        headlineContent = { Text(item.wallet.name) },
        supportingContent = { Text(stringResource(item.wallet.type.labelRes())) },
        trailingContent = {
            Text(
                text = formatAmount(item.balance, locale),
                style = MaterialTheme.typography.titleMedium,
                color = if (item.balance < 0) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        },
        modifier = modifier,
    )
}

private val previewWallets = listOf(
    WalletWithBalance(Wallet(1, "Hotovost", WalletType.CASH, initialBalance = 0), balance = 245_050),
    WalletWithBalance(Wallet(2, "Fio", WalletType.BANK, initialBalance = 0), balance = 3_850_000),
    WalletWithBalance(Wallet(3, "Rezerva", WalletType.SAVINGS, initialBalance = 0), balance = -10_000),
)

@Preview(showBackground = true, showSystemUi = true, locale = "cs")
@Composable
private fun WalletsScreenPreview() {
    LedgerlyTheme {
        WalletsScreen(
            uiState = WalletsUiState(
                wallets = previewWallets,
                totalBalance = previewWallets.sumOf { it.balance },
                isLoading = false,
            ),
            onAddWalletClick = {},
        )
    }
}
