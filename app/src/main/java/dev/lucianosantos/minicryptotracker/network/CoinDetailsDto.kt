package dev.lucianosantos.minicryptotracker.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsDto(
    val id: String,
    val symbol: String,
    val name: String,
    val description: Map<String, String>,
    @SerialName("market_data")
    val marketData: MarketDataDto,
    val image: ImageDto
)

@Serializable
data class ImageDto(
    val small: String,
    val thumb: String,
    val large: String
)

@Serializable
data class MarketDataDto(
    @SerialName("current_price")
    val currentPrice: Map<String, Double>
)