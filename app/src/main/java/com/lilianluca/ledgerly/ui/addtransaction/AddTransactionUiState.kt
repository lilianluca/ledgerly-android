package com.lilianluca.ledgerly.ui.addtransaction

import com.lilianluca.ledgerly.domain.model.TransactionType
import java.time.LocalDate

/** State of the add-transaction form. Field values are kept as typed by the user. */
data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    /** Raw text from the amount field; converted to minor units only on save. */
    val amountText: String = "",
    val category: String = "",
    val date: LocalDate = LocalDate.now(),
    val note: String = "",
    val amountError: AmountError? = null,
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
