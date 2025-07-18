package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.database.CryptoDao
import dev.lucianosantos.minicryptotracker.database.CryptoEntity
import dev.lucianosantos.minicryptotracker.model.AppError
import dev.lucianosantos.minicryptotracker.model.AppException
import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.model.toAppError
import dev.lucianosantos.minicryptotracker.network.CoinDetailsDto
import dev.lucianosantos.minicryptotracker.network.CoinDto
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import dev.lucianosantos.minicryptotracker.network.ErrorStatusDto
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class CryptoRepositoryImpl(
    private val cryptoDao: CryptoDao,
    private val coinGeckoAPI: CoinGeckoAPI
) : CryptoRepository {

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

    override suspend fun syncRemote(): Result<Unit> {
        return try {
            val response = coinGeckoAPI.getCoinsList()
            if (response.isSuccessful) {
                val items = response.body()?.map { it.toDomain() }.orEmpty()
                cacheItems(items)
                Result.success(Unit)
            } else {
                val appError = response.toAppError()
                Result.failure(AppException(appError))
            }
        } catch (e: IOException) {
            Result.failure(AppException(AppError.Network))
        } catch (e: HttpException) {
            val appError = e.toAppError()
            Result.failure(AppException(appError))
        } catch (e: Exception) {
            Result.failure(AppException(AppError.Unknown))
        }
    }

    override suspend fun getDetails(id: String): Result<CryptoDomain> {
        return try {
            val response = coinGeckoAPI.getCoinDetails(id)
            if (response.isSuccessful) {
                val coin = response.body()?.toDomain()
                    ?: return Result.failure(AppException(AppError.UnexpectedResponse))
                cacheDetails(coin)
                Result.success(coin)
            } else {
                val appError = response.toAppError()
                Result.failure(AppException(appError))
            }
        } catch (e: IOException) {
            Result.failure(AppException(AppError.Network))
        } catch (e: HttpException) {
            val appError = e.toAppError()
            Result.failure(AppException(appError))
        } catch (e: Exception) {
            Result.failure(AppException(AppError.Unknown))
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