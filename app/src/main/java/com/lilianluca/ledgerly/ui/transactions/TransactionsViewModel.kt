package com.lilianluca.ledgerly.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Holds the state of the transactions screen and survives configuration changes. */
@HiltViewModel
class TransactionsViewModel @Inject constructor(
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
}
