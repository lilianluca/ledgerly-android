package com.lilianluca.ledgerly.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lilianluca.ledgerly.LedgerlyApplication
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Holds the state of the transactions screen and survives configuration changes. */
class TransactionsViewModel(
    transactionRepository: TransactionRepository,
) : ViewModel() {

    val uiState: StateFlow<TransactionsUiState> = transactionRepository.observeTransactions()
        .map { transactions -> TransactionsUiState(transactions = transactions, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            // Keep the upstream alive for 5 s after the UI stops observing (e.g. during rotation)
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionsUiState(),
        )

    companion object {
        // Manual DI: pulls the repository from the app container. Replaced by Hilt in milestone 4.
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as LedgerlyApplication
                TransactionsViewModel(app.container.transactionRepository)
            }
        }
    }
}
