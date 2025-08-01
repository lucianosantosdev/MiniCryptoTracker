package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    val cryptoCoins: Flow<List<CryptoDomain>>
    suspend fun syncRemote(): Result<Unit>
    suspend fun getDetails(id: String): Result<CryptoDomain>
}