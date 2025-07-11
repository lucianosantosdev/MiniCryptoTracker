package dev.lucianosantos.minicryptotracker.ui

data class CryptoDomain(
    val id: String,
    val name: String,
    val symbol: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val currentPrice: Double? = null,
)