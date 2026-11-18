package com.lilianluca.ledgerly.ui.addwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.WalletRepository
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletType
import com.lilianluca.ledgerly.util.parseAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** State of the add-wallet form. */
data class AddWalletUiState(
    val name: String = "",
    // A cash wallet exists from the start, so a bank account is the most likely new one
    val type: WalletType = WalletType.BANK,
    /** Raw text from the field; empty means zero. */
    val initialBalanceText: String = "",
    val nameError: Boolean = false,
    val initialBalanceError: Boolean = false,
    val isSaving: Boolean = false,
    /** True once the wallet is stored; the UI reacts by navigating back. */
    val isSaved: Boolean = false,
)

/** Holds the form state, validates input and saves the new wallet. */
@HiltViewModel
class AddWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddWalletUiState())
    val uiState: StateFlow<AddWalletUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = false) }
    }

    fun onTypeChange(type: WalletType) {
        _uiState.update { it.copy(type = type) }
    }

    fun onInitialBalanceChange(text: String) {
        _uiState.update { it.copy(initialBalanceText = text, initialBalanceError = false) }
    }

    fun onSave() {
        val state = _uiState.value
        if (state.isSaving || state.isSaved) return // ignore repeated taps

        val initialBalance = if (state.initialBalanceText.isBlank()) {
            0L
        } else {
            parseAmount(state.initialBalanceText)
        }
        val nameError = state.name.isBlank()

        if (initialBalance == null || nameError) {
            _uiState.update {
                it.copy(nameError = nameError, initialBalanceError = initialBalance == null)
            }
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            walletRepository.addWallet(
                Wallet(
                    id = 0, // assigned by the database
                    name = state.name.trim(),
                    type = state.type,
                    initialBalance = initialBalance,
                ),
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
