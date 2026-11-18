package com.lilianluca.ledgerly.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.ui.addtransaction.AddTransactionRoute
import com.lilianluca.ledgerly.ui.addwallet.AddWalletRoute
import com.lilianluca.ledgerly.ui.transactions.TransactionsRoute
import com.lilianluca.ledgerly.ui.wallets.WalletsRoute

/** A main screen reachable from the bottom navigation bar. */
private data class TopLevelDestination(
    val key: NavKey,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(TransactionsNavKey, R.string.transactions_title, R.drawable.ic_receipt_long),
    TopLevelDestination(WalletsNavKey, R.string.wallets_title, R.drawable.ic_wallet),
)

/** App navigation: owns the back stack, maps each key to its screen and shows the bottom bar. */
@Composable
fun LedgerlyNavDisplay(modifier: Modifier = Modifier) {
    // A plain list of keys we own; the last one is the visible screen.
    // Main screens are either [Transactions] or [Transactions, Wallets], so Back from Wallets
    // returns to Transactions and only then leaves the app.
    val backStack = rememberNavBackStack(TransactionsNavKey)
    val currentKey = backStack.lastOrNull()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            // Forms are shown without the bar
            if (topLevelDestinations.any { it.key == currentKey }) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = destination.key == currentKey,
                            onClick = {
                                when {
                                    destination.key == currentKey -> Unit
                                    destination.key == WalletsNavKey -> backStack.add(WalletsNavKey)
                                    else -> backStack.removeLastOrNull()
                                }
                            },
                            icon = {
                                // The label below already describes the icon
                                Icon(painterResource(destination.iconRes), contentDescription = null)
                            },
                            label = { Text(stringResource(destination.labelRes)) },
                        )
                    }
                }
            }
        },
        // System bars are handled by the Scaffold of each screen
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier
                .padding(innerPadding)
                // The bar already covers the navigation bar inset; screens must not add it again
                .consumeWindowInsets(innerPadding),
            entryDecorators = listOf(
                // Saves rememberSaveable state per screen
                rememberSaveableStateHolderNavEntryDecorator(),
                // Gives each screen its own ViewModels, cleared when the screen leaves the back stack
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<TransactionsNavKey> {
                    TransactionsRoute(
                        // Ignores clicks while a transition is running, preventing double navigation
                        onAddTransactionClick = dropUnlessResumed { backStack.add(AddTransactionNavKey) },
                    )
                }
                entry<AddTransactionNavKey> {
                    AddTransactionRoute(
                        onNavigateBack = dropUnlessResumed { backStack.removeLastOrNull() },
                    )
                }
                entry<WalletsNavKey> {
                    WalletsRoute(
                        onAddWalletClick = dropUnlessResumed { backStack.add(AddWalletNavKey) },
                    )
                }
                entry<AddWalletNavKey> {
                    AddWalletRoute(
                        onNavigateBack = dropUnlessResumed { backStack.removeLastOrNull() },
                    )
                }
            },
        )
    }
}
