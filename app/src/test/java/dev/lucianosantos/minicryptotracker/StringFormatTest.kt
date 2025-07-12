package dev.lucianosantos.minicryptotracker

import dev.lucianosantos.minicryptotracker.utils.toUsdCurrencyString
import org.junit.Assert.assertEquals
import org.junit.Test

class StringFormatTest {
    @Test
    fun `When converting a Double to USD currency string, then it should format correctly`() {
        assertEquals("$0.00", (0.0).toUsdCurrencyString())
        assertEquals("$123.45", (123.45).toUsdCurrencyString())
        assertEquals("$1,234.56", (1234.56).toUsdCurrencyString())
        assertEquals("$1,234,567.89", (1234567.89).toUsdCurrencyString())
    }

    @Test
    fun `When converting a Double with lots of decimal places to USD currency string, then it should keep precision`() {
        assertEquals("$123.456789", (123.456789).toUsdCurrencyString())
        assertEquals("$0.000001", (0.000001).toUsdCurrencyString())
        assertEquals("$1,234,567.890123", (1234567.890123).toUsdCurrencyString())
    }
}