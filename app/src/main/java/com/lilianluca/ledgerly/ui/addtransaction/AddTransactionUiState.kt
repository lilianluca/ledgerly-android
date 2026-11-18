package com.lilianluca.ledgerly.ui.addtransaction

import com.lilianluca.ledgerly.domain.model.TransactionType
import com.lilianluca.ledgerly.domain.model.Wallet
import java.time.LocalDate

/** State of the add-transaction form. Field values are kept as typed by the user. */
data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    /** Raw text from the amount field; converted to minor units only on save. */
    val amountText: String = "",
    /** Wallets the user can choose from. */
    val wallets: List<Wallet> = emptyList(),
    /** Selected wallet; null only until the wallets are loaded. */
    val walletId: Long? = null,
    /** Destination wallet; used only for transfers. */
    val toWalletId: Long? = null,
    val category: String = "",
    val date: LocalDate = LocalDate.now(),
    val note: String = "",
    val amountError: AmountError? = null,
    val toWalletError: ToWalletError? = null,
    val categoryError: Boolean = false,
    val isSaving: Boolean = false,
    /** True once the transaction is stored; the UI reacts by navigating back. */
    val isSaved: Boolean = false,
)

/** Validation errors for the amount field. The UI maps them to localized messages. */
enum class AmountError {
    EMPTY,
    INVALID,
    ZERO,
}

/** Validation errors for the destination wallet of a transfer. */
enum class ToWalletError {
    MISSING,
    NO_OTHER_WALLET,
}
