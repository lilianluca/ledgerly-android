package com.lilianluca.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lilianluca.ledgerly.data.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

/** Database queries for wallets. */
@Dao
interface WalletDao {

    /** Live query of all wallets, oldest first (the default wallet is always first). */
    @Query("SELECT * FROM wallets ORDER BY id")
    fun observeAll(): Flow<List<WalletEntity>>

    @Insert
    suspend fun insert(wallet: WalletEntity): Long
}
