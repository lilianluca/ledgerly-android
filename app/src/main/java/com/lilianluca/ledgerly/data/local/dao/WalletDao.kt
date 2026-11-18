package com.lilianluca.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lilianluca.ledgerly.data.local.entity.WalletEntity
import com.lilianluca.ledgerly.data.local.entity.WalletWithBalanceRow
import kotlinx.coroutines.flow.Flow

/** Database queries for wallets. */
@Dao
interface WalletDao {

    /** Live query of non-archived wallets, oldest first (the default wallet comes first). */
    @Query("SELECT * FROM wallets WHERE isArchived = 0 ORDER BY id")
    fun observeActive(): Flow<List<WalletEntity>>

    /**
     * Live query of non-archived wallets with their balances.
     *
     * The balance is always computed, never stored, so it can't get out of sync with transactions.
     * COALESCE is needed because SUM over no rows returns NULL. A transfer is subtracted from
     * its source wallet and added to its destination wallet.
     * Re-runs whenever either the "wallets" or the "transactions" table changes.
     */
    @Query(
        """
        SELECT w.*,
            w.initialBalance
            + COALESCE((SELECT SUM(t.amount) FROM transactions t
                        WHERE t.walletId = w.id AND t.type = 'INCOME'), 0)
            - COALESCE((SELECT SUM(t.amount) FROM transactions t
                        WHERE t.walletId = w.id AND t.type IN ('EXPENSE', 'TRANSFER')), 0)
            + COALESCE((SELECT SUM(t.amount) FROM transactions t
                        WHERE t.toWalletId = w.id AND t.type = 'TRANSFER'), 0)
            AS balance
        FROM wallets w
        WHERE w.isArchived = 0
        ORDER BY w.id
        """,
    )
    fun observeActiveWithBalance(): Flow<List<WalletWithBalanceRow>>

    @Insert
    suspend fun insert(wallet: WalletEntity): Long
}
