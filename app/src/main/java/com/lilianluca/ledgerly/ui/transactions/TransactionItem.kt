package com.lilianluca.ledgerly.ui.transactions

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.TransactionType
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme
import com.lilianluca.ledgerly.util.formatAmount
import com.lilianluca.ledgerly.util.formatDate

/** A single row in the transaction list: category, date with wallet and note, signed amount. */
@Composable
fun TransactionItem(
    item: TransactionListItem,
    modifier: Modifier = Modifier,
) {
    val transaction = item.transaction
    // Current app locale; reading it here makes the item update when the language changes
    val locale = LocalConfiguration.current.locales[0]

    val title = transaction.category ?: stringResource(R.string.transaction_type_transfer)
    // "Cash" for income and expenses, "Bank → Cash" for transfers
    val wallets = listOfNotNull(item.walletName, item.toWalletName)
        .joinToString(" → ")
        .ifEmpty { null }
    val details = listOfNotNull(formatDate(transaction.date, locale), wallets, transaction.note)
        .joinToString(" · ")

    val amount = formatAmount(transaction.amount, locale)
    val (amountText, amountColor) = when (transaction.type) {
        TransactionType.INCOME -> "+$amount" to MaterialTheme.colorScheme.primary
        TransactionType.EXPENSE -> "−$amount" to MaterialTheme.colorScheme.error // minus sign
        TransactionType.TRANSFER -> amount to MaterialTheme.colorScheme.onSurfaceVariant
    }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = {
            Text(
                text = details,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = {
            Text(
                text = amountText,
                color = amountColor,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemIncomePreview() {
    LedgerlyTheme {
        TransactionItem(sampleTransactionItems.first { it.transaction.type == TransactionType.INCOME })
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemExpensePreview() {
    LedgerlyTheme {
        TransactionItem(sampleTransactionItems.first { it.transaction.type == TransactionType.EXPENSE })
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemTransferPreview() {
    LedgerlyTheme {
        TransactionItem(sampleTransactionItems.first { it.transaction.type == TransactionType.TRANSFER })
    }
}

@Preview(showBackground = true, locale = "cs")
@Composable
private fun TransactionItemCzechPreview() {
    LedgerlyTheme {
        TransactionItem(sampleTransactionItems.first { it.transaction.type == TransactionType.TRANSFER })
    }
}
