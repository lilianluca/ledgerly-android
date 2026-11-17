package com.lilianluca.ledgerly.data.local

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lilianluca.ledgerly.domain.model.Wallet
import com.lilianluca.ledgerly.domain.model.WalletType

// SQL below is copied from the exported schema (app/schemas/.../2.json). Room validates the
// migrated database against that schema on startup, so it must match exactly.

private const val CREATE_WALLETS_TABLE =
    "CREATE TABLE IF NOT EXISTS `wallets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
        "`name` TEXT NOT NULL, `type` TEXT NOT NULL, `initialBalance` INTEGER NOT NULL, " +
        "`isArchived` INTEGER NOT NULL)"

/** Inserts the default wallet that must always exist (see [Wallet.DEFAULT_ID]). */
private fun SupportSQLiteDatabase.insertDefaultWallet(name: String) {
    execSQL(
        "INSERT INTO `wallets` (`id`, `name`, `type`, `initialBalance`, `isArchived`) VALUES (?, ?, ?, 0, 0)",
        arrayOf<Any>(Wallet.DEFAULT_ID, name, WalletType.CASH.name),
    )
}

/**
 * Version 1 → 2: adds wallets and assigns every existing transaction to a new default wallet.
 *
 * SQLite can't add a foreign key to an existing table, so the transactions table is recreated:
 * create new table → copy rows → drop old table → rename (the officially recommended approach).
 *
 * @param defaultWalletName localized name of the default wallet, e.g. "Hotovost"
 */
class Migration1To2(private val defaultWalletName: String) : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(CREATE_WALLETS_TABLE)
        db.insertDefaultWallet(defaultWalletName)

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `transactions_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`type` TEXT NOT NULL, `amount` INTEGER NOT NULL, `walletId` INTEGER NOT NULL, " +
                "`toWalletId` INTEGER, `category` TEXT, `date` TEXT NOT NULL, `note` TEXT, " +
                "FOREIGN KEY(`walletId`) REFERENCES `wallets`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , " +
                "FOREIGN KEY(`toWalletId`) REFERENCES `wallets`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )",
        )
        // Every old transaction goes to the default wallet. Old transfers had no destination,
        // so they become a transfer within the same wallet: valid data, no effect on balances.
        db.execSQL(
            "INSERT INTO `transactions_new` (`id`, `type`, `amount`, `walletId`, `toWalletId`, `category`, `date`, `note`) " +
                "SELECT `id`, `type`, `amount`, ${Wallet.DEFAULT_ID}, " +
                "CASE WHEN `type` = 'TRANSFER' THEN ${Wallet.DEFAULT_ID} ELSE NULL END, " +
                "`category`, `date`, `note` FROM `transactions`",
        )
        db.execSQL("DROP TABLE `transactions`")
        db.execSQL("ALTER TABLE `transactions_new` RENAME TO `transactions`")

        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_date` ON `transactions` (`date`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_walletId` ON `transactions` (`walletId`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_toWalletId` ON `transactions` (`toWalletId`)")
    }
}

/**
 * Runs only when the database file is created for the first time (fresh install).
 * Migrations don't run in that case, so the default wallet is inserted here as well.
 */
class DefaultWalletCallback(private val defaultWalletName: String) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.insertDefaultWallet(defaultWalletName)
    }
}
