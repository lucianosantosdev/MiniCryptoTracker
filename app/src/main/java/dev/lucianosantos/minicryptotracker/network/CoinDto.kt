package dev.lucianosantos.minicryptotracker.network

import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String
)
