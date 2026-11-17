package com.lilianluca.ledgerly.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Keys identify destinations in the back stack (like URLs in a web router).
// @Serializable lets the back stack survive configuration changes and process death.

@Serializable
data object TransactionsNavKey : NavKey

@Serializable
data object AddTransactionNavKey : NavKey
