package dev.lucianosantos.minicryptotracker.ui

data class CryptoItem(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val description: String,
    val currentPrice: Long,
)