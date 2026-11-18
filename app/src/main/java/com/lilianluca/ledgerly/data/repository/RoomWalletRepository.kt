package com.lilianluca.ledgerly.data.repository

import com.lilianluca.ledgerly.data.local.dao.WalletDao
import com.lilianluca.ledgerly.data.local.entity.toDomain
import com.lilianluca.ledgerly.data.local.entity.toEntity
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletWithBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Stores wallets in the Room database. */
class RoomWalletRepository @Inject constructor(
    private val walletDao: WalletDao,
) : WalletRepository {

    override fun observeWallets(): Flow<List<Wallet>> =
        walletDao.observeActive().map { rows -> rows.map { it.toDomain() } }

    override fun observeWalletsWithBalance(): Flow<List<WalletWithBalance>> =
        walletDao.observeActiveWithBalance().map { rows -> rows.map { it.toDomain() } }

    override suspend fun addWallet(wallet: Wallet) {
        // id = 0 lets SQLite assign a new id
        walletDao.insert(wallet.copy(id = 0).toEntity())
    }
}
