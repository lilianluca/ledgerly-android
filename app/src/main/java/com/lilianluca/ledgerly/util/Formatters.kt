package com.lilianluca.ledgerly.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

/**
 * Formats an amount in minor units (haléře) as a localized CZK string,
 * e.g. "18 000,00 Kč" (cs) or "CZK 18,000.00" (en).
 */
fun formatAmount(amount: Long, locale: Locale): String {
    val format = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance("CZK")
    }
    // BigDecimal keeps the value exact, unlike amount / 100.0
    return format.format(BigDecimal.valueOf(amount, 2))
        .replace('-', '−') // typographic minus, same as in the transaction list
}

/** Formats a date in the medium localized style, e.g. "16. 11. 2026" (cs) or "Nov 16, 2026" (en). */
fun formatDate(date: LocalDate, locale: Locale): String =
    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale))
