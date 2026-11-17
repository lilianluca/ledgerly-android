package com.lilianluca.ledgerly.ui.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.domain.model.TransactionType
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.util.parseAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/** Holds the form state, validates input and saves the new transaction. */
@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    // Each handler changes one field and clears its error, so the message disappears while typing

    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(type = type, categoryError = false) }
    }

    fun onAmountChange(text: String) {
        _uiState.update { it.copy(amountText = text, amountError = null) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category, categoryError = false) }
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(date = date) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onSave() {
        val state = _uiState.value
        if (state.isSaving || state.isSaved) return // ignore repeated taps

        val amount = parseAmount(state.amountText)
        val amountError = when {
            state.amountText.isBlank() -> AmountError.EMPTY
            amount == null -> AmountError.INVALID
            amount <= 0 -> AmountError.ZERO
            else -> null
        }
        val needsCategory = state.type != TransactionType.TRANSFER
        val categoryError = needsCategory && state.category.isBlank()

        if (amount == null || amountError != null || categoryError) {
            _uiState.update { it.copy(amountError = amountError, categoryError = categoryError) }
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            transactionRepository.addTransaction(
                Transaction(
                    id = 0, // assigned by the repository
                    type = state.type,
                    amount = amount,
                    // Temporary until the wallet picker (step 5.4): everything goes to the default wallet
                    walletId = Wallet.DEFAULT_ID,
                    toWalletId = if (state.type == TransactionType.TRANSFER) Wallet.DEFAULT_ID else null,
                    category = if (needsCategory) state.category.trim() else null,
                    date = state.date,
                    note = state.note.trim().ifBlank { null },
                ),
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
