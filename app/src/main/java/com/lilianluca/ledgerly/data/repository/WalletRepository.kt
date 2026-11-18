package com.lilianluca.ledgerly.data.repository

import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletWithBalance
import kotlinx.coroutines.flow.Flow

/** Single source of truth for wallets. */
interface WalletRepository {

    /** Emits non-archived wallets (default wallet first) and every subsequent change. */
    fun observeWallets(): Flow<List<Wallet>>

    /** Like [observeWallets], with balances; also updates when transactions change. */
    fun observeWalletsWithBalance(): Flow<List<WalletWithBalance>>

    /** Adds a new wallet. The passed [Wallet.id] is ignored; the database assigns one. */
    suspend fun addWallet(wallet: Wallet)
}
