package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.ui.CryptoItem
import kotlinx.coroutines.flow.Flow

class CryptoRepositoryImpl: CryptoRepository {
    override suspend fun fetchCryptoItems(): List<CryptoItem> {
        return listOf(
            CryptoItem(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://example.com/bitcoin.png",
                description = "Bitcoin is a decentralized digital currency.",
                currentPrice = 50000L
            ),
            CryptoItem(
                id = "2",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://example.com/ethereum.png",
                description = "Ethereum is a decentralized platform for smart contracts.",
                currentPrice = 3000L
            )
        )
    }

    override suspend fun fetchCryptoItemById(id: String): Flow<CryptoItem> {
        TODO("Not yet implemented")
    }
}