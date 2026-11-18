package com.lilianluca.ledgerly.ui.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.TransactionRepository
import com.lilianluca.ledgerly.data.repository.WalletRepository
import com.lilianluca.ledgerly.domain.model.Transaction
import com.lilianluca.ledgerly.domain.model.TransactionType
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
    walletRepository: WalletRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            walletRepository.observeWallets().collect { wallets ->
                _uiState.update { state ->
                    // Keep the user's choice if still valid, otherwise preselect the first wallet
                    val walletId = state.walletId?.takeIf { id -> wallets.any { it.id == id } }
                        ?: wallets.firstOrNull()?.id
                    state.copy(wallets = wallets, walletId = walletId)
                }
            }
        }
    }

    // Each handler changes one field and clears its error, so the message disappears while typing

    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(type = type, categoryError = false, toWalletError = null) }
    }

    fun onAmountChange(text: String) {
        _uiState.update { it.copy(amountText = text, amountError = null) }
    }

    fun onWalletChange(walletId: Long) {
        _uiState.update { state ->
            state.copy(
                walletId = walletId,
                // A transfer can't go to the wallet it comes from
                toWalletId = state.toWalletId.takeUnless { it == walletId },
            )
        }
    }

    fun onToWalletChange(walletId: Long) {
        _uiState.update { it.copy(toWalletId = walletId, toWalletError = null) }
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
        val walletId = state.walletId ?: return // wallets not loaded yet

        val amount = parseAmount(state.amountText)
        val amountError = when {
            state.amountText.isBlank() -> AmountError.EMPTY
            amount == null -> AmountError.INVALID
            amount <= 0 -> AmountError.ZERO
            else -> null
        }
        val isTransfer = state.type == TransactionType.TRANSFER
        val toWalletError = when {
            !isTransfer -> null
            state.wallets.size < 2 -> ToWalletError.NO_OTHER_WALLET
            state.toWalletId == null || state.toWalletId == walletId -> ToWalletError.MISSING
            else -> null
        }
        val categoryError = !isTransfer && state.category.isBlank()

        if (amount == null || amountError != null || toWalletError != null || categoryError) {
            _uiState.update {
                it.copy(
                    amountError = amountError,
                    toWalletError = toWalletError,
                    categoryError = categoryError,
                )
            }
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            transactionRepository.addTransaction(
                Transaction(
                    id = 0, // assigned by the repository
                    type = state.type,
                    amount = amount,
                    walletId = walletId,
                    toWalletId = if (isTransfer) state.toWalletId else null,
                    category = if (isTransfer) null else state.category.trim(),
                    date = state.date,
                    note = state.note.trim().ifBlank { null },
                ),
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
