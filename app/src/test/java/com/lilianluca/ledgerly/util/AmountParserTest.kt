package com.lilianluca.ledgerly.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AmountParserTest {

    // --- Valid input ---

    @Test
    fun `parses whole number into minor units`() {
        assertEquals(34_900L, parseAmount("349"))
    }

    @Test
    fun `accepts comma as decimal separator`() {
        assertEquals(34_950L, parseAmount("349,5"))
        assertEquals(34_950L, parseAmount("349,50"))
    }

    @Test
    fun `accepts dot as decimal separator`() {
        assertEquals(34_950L, parseAmount("349.50"))
    }

    @Test
    fun `parses smallest amount`() {
        assertEquals(1L, parseAmount("0,01"))
    }

    @Test
    fun `ignores spaces, including thousands separators`() {
        assertEquals(125_000L, parseAmount("1 250"))
        assertEquals(125_000L, parseAmount("  1 250,00  "))
        assertEquals(125_000L, parseAmount("1 250")) // non-breaking space, e.g. from copy-paste
    }

    @Test
    fun `parses zero, rejecting it is up to validation`() {
        assertEquals(0L, parseAmount("0"))
    }

    // --- Invalid input ---

    @Test
    fun `returns null for empty or blank input`() {
        assertNull(parseAmount(""))
        assertNull(parseAmount("   "))
    }

    @Test
    fun `returns null for non-numeric input`() {
        assertNull(parseAmount("abc"))
        assertNull(parseAmount("12a"))
    }

    @Test
    fun `returns null for more than two decimal places`() {
        assertNull(parseAmount("12,345"))
    }

    @Test
    fun `returns null for negative numbers`() {
        assertNull(parseAmount("-50"))
    }

    @Test
    fun `returns null for scientific notation`() {
        assertNull(parseAmount("1e5"))
    }

    @Test
    fun `returns null for incomplete decimals`() {
        assertNull(parseAmount("349."))
        assertNull(parseAmount(",5"))
    }

    @Test
    fun `returns null for multiple decimal separators`() {
        assertNull(parseAmount("1,234.56"))
    }

    @Test
    fun `returns null when the value does not fit into Long`() {
        assertNull(parseAmount("999999999999999999999"))
    }
}
