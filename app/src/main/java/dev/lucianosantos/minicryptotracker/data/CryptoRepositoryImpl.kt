package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.database.CryptoDao
import dev.lucianosantos.minicryptotracker.database.CryptoEntity
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import dev.lucianosantos.minicryptotracker.ui.CryptoDomain
import kotlinx.coroutines.flow.Flow
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
                    CryptoDomain(
                        id = coin.id,
                        name = coin.name,
                        symbol = coin.symbol,
                        imageUrl = "",
                        description = "",
                        currentPrice = 0L
                    )
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

    override suspend fun getDetails(id: String): CryptoDomain {
        TODO("Not yet implemented")
    }

    private suspend fun cacheItems(cryptos: List<CryptoDomain>) {
        cryptoDao.insertAll(cryptos.map { it.toEntity() })
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