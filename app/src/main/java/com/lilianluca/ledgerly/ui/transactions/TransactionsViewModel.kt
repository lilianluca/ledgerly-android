package com.lilianluca.ledgerly.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import com.lilianluca.ledgerly.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Holds the state of the transactions screen and survives configuration changes. */
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    transactionRepository: TransactionRepository,
    walletRepository: WalletRepository,
) : ViewModel() {

    // combine re-runs the block whenever either source emits a new value
    val uiState: StateFlow<TransactionsUiState> = combine(
        transactionRepository.observeTransactions(),
        walletRepository.observeAllWallets(),
    ) { transactions, wallets ->
        val walletNames = wallets.associate { it.id to it.name }
        TransactionsUiState(
            transactions = transactions.map { transaction ->
                TransactionListItem(
                    transaction = transaction,
                    walletName = walletNames[transaction.walletId],
                    toWalletName = transaction.toWalletId?.let { walletNames[it] },
                )
            },
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        // Keep the upstream alive for 5 s after the UI stops observing (e.g. during rotation)
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionsUiState(),
    )
}
