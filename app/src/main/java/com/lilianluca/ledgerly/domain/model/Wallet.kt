package com.lilianluca.ledgerly.domain.model

/** A place where money is kept, e.g. cash or a bank account. */
data class Wallet(
    val id: Long,
    val name: String,
    val type: WalletType,
    /** Balance at the moment the wallet was added to the app, in minor units (haléře). */
    val initialBalance: Long,
    /** Archived wallets are hidden from pickers but keep their transaction history. */
    val isArchived: Boolean = false,
) {
    companion object {
        /** The wallet created on first launch (and by the 1 → 2 migration); always exists. */
        const val DEFAULT_ID = 1L
    }
}

enum class WalletType {
    CASH,
    BANK,
    SAVINGS,
}
