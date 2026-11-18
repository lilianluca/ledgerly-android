package com.lilianluca.ledgerly.domain.model

/** A wallet together with its current balance, computed from its transactions. */
data class WalletWithBalance(
    val wallet: Wallet,
    /** Current balance in minor units (haléře); can be negative. */
    val balance: Long,
)
