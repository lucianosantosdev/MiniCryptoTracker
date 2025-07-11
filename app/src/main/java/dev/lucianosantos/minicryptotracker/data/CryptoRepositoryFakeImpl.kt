package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.ui.CryptoDomain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CryptoRepositoryFakeImpl: CryptoRepository {
    private val fakeCoins = listOf(
        CryptoDomain(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://example.com/bitcoin.png",
            description = "Bitcoin is a decentralized digital currency.",
            currentPrice = 50000L
        ),
        CryptoDomain(
            id = "2",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://example.com/ethereum.png",
            description = "Ethereum is a decentralized platform for smart contracts.",
            currentPrice = 3000L
        )
    )

    override val cryptoCoins: Flow<List<CryptoDomain>>
        get() = flowOf(fakeCoins)

    override suspend fun syncRemote(): Result<List<CryptoDomain>> {
        delay(500)
        return Result.success(fakeCoins)
    }

    override suspend fun getDetails(id: String): CryptoDomain {
        delay(200) // Simulate loading
        return fakeCoins.first()
    }
}