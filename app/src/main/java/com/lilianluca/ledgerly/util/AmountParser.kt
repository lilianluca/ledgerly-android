package com.lilianluca.ledgerly.util

import java.math.BigDecimal

// Digits, optionally followed by a decimal point and 1–2 decimal digits (after normalization)
private val amountPattern = Regex("""\d+(\.\d{1,2})?""")

/**
 * Parses user input such as "349", "349,5", "349.50" or "1 250" into minor units (haléře).
 *
 * Accepts both comma and dot as the decimal separator and ignores whitespace.
 * Returns null for empty, malformed, negative or too precise (more than 2 decimals) input.
 */
fun parseAmount(text: String): Long? {
    val normalized = text.filterNot { it.isWhitespace() }.replace(',', '.')
    if (!amountPattern.matches(normalized)) return null

    return try {
        // BigDecimal keeps the value exact; longValueExact throws on overflow
        BigDecimal(normalized).movePointRight(2).longValueExact()
    } catch (_: ArithmeticException) {
        null
    }
}
