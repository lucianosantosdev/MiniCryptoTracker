package dev.lucianosantos.minicryptotracker.model

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
            currentPrice = 50000.0
        ),
        CryptoDomain(
            id = "2",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://example.com/ethereum.png",
            description = "Ethereum is a decentralized platform for smart contracts.",
            currentPrice = 3000.0
        )
    )

    override val cryptoCoins: Flow<List<CryptoDomain>>
        get() = flowOf(fakeCoins)

    override suspend fun syncRemote(): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    override suspend fun getDetails(id: String): Result<CryptoDomain> {
        delay(1000)
        return Result.success(fakeCoins.first())
    }
}