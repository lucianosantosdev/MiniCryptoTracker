package dev.lucianosantos.minicryptotracker.model

import dev.lucianosantos.minicryptotracker.database.CryptoDao
import dev.lucianosantos.minicryptotracker.database.CryptoEntity
import dev.lucianosantos.minicryptotracker.network.CoinDetailsDto
import dev.lucianosantos.minicryptotracker.network.CoinDto
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import kotlinx.coroutines.flow.map

class CryptoRepositoryImpl(
    private val cryptoDao: CryptoDao,
    private val coinGeckoAPI: CoinGeckoAPI
): CryptoRepository {

    override val cryptoCoins = cryptoDao.getAll().map { entities ->
        entities.map { entity ->
            CryptoDomain(
                id = entity.id,
                name = entity.name,
                symbol = entity.symbol,
                imageUrl = entity.image,
                description = entity.description
            )
        }
    }

    override suspend fun syncRemote(): Result<List<CryptoDomain>> {
        return try {
            val response = coinGeckoAPI.getCoinsList()
            if (response.isSuccessful) {
                val items = response.body()?.map { coin ->
                    coin.toDomain()
                } ?: emptyList()
                cacheItems(items)
                Result.success(items)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Server error: ${response.code()} - ${errorBody ?: "Unknown"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDetails(id: String): Result<CryptoDomain> {
        return try {
            val response = coinGeckoAPI.getCoinDetails(id)
            if (response.isSuccessful) {
                val coin = response.body()?.toDomain() ?: return Result.failure(Exception("Coin not found"))
                cacheDetails(coin)
                Result.success(coin)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Server error: ${response.code()} - ${errorBody ?: "Unknown"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun cacheItems(cryptos: List<CryptoDomain>) {
        cryptoDao.insertAll(cryptos.map { it.toEntity() })
    }

    private suspend fun cacheDetails(crypto: CryptoDomain) {
        cryptoDao.update(crypto.toEntity())
    }

    private fun CoinDto.toDomain(): CryptoDomain {
        return CryptoDomain(
            id = this.id,
            name = this.name,
            symbol = this.symbol
        )
    }

    private fun CoinDetailsDto.toDomain(): CryptoDomain {
        return CryptoDomain(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            imageUrl = this.image.small,
            description = this.description["en"],
            currentPrice = this.marketData.currentPrice["usd"]
        )
    }

    private fun CryptoDomain.toEntity() = CryptoEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        image = this.imageUrl,
        description = this.description,
        price = this.currentPrice
    )
}