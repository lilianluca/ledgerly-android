package com.lilianluca.ledgerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lilianluca.ledgerly.ui.addtransaction.AddTransactionScreen
import com.lilianluca.ledgerly.ui.transactions.TransactionsRoute

/** App navigation: owns the back stack and maps each key to its screen. */
@Composable
fun LedgerlyNavDisplay(modifier: Modifier = Modifier) {
    // A plain list of keys we own; the last one is the visible screen
    val backStack = rememberNavBackStack(TransactionsNavKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
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
                AddTransactionScreen(
                    onNavigateBack = dropUnlessResumed { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
