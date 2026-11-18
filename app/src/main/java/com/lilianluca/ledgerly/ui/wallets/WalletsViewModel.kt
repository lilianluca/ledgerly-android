package com.lilianluca.ledgerly.ui.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilianluca.ledgerly.data.repository.WalletRepository
import com.lilianluca.ledgerly.domain.model.WalletWithBalance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Everything the wallets screen needs to render. */
data class WalletsUiState(
    val wallets: List<WalletWithBalance> = emptyList(),
    /** Sum of all wallet balances in minor units (haléře). */
    val totalBalance: Long = 0,
    val isLoading: Boolean = true,
)

/** Provides wallets with their balances; updates automatically when transactions change. */
@HiltViewModel
class WalletsViewModel @Inject constructor(
    walletRepository: WalletRepository,
) : ViewModel() {

    val uiState: StateFlow<WalletsUiState> = walletRepository.observeWalletsWithBalance()
        .map { wallets ->
            WalletsUiState(
                wallets = wallets,
                totalBalance = wallets.sumOf { it.balance },
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WalletsUiState(),
        )
}
