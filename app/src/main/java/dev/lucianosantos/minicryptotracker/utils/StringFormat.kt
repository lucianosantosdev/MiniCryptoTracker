package dev.lucianosantos.minicryptotracker.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun Double.toUsdCurrencyString(): String {
    val usSymbols = DecimalFormatSymbols(Locale.US)
    val pattern = "\$#,##0.##########"
    val formatter = DecimalFormat(pattern, usSymbols).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 10
        isGroupingUsed = true
    }
    return formatter.format(this)
}