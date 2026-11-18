package com.lilianluca.ledgerly.ui.wallets

import androidx.annotation.StringRes
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.WalletType

/** Localized label of a wallet type. */
@StringRes
fun WalletType.labelRes(): Int = when (this) {
    WalletType.CASH -> R.string.wallet_type_cash
    WalletType.BANK -> R.string.wallet_type_bank
    WalletType.SAVINGS -> R.string.wallet_type_savings
}
