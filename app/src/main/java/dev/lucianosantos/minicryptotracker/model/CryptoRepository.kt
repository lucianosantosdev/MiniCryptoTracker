package dev.lucianosantos.minicryptotracker.model

import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    val cryptoCoins: Flow<List<CryptoDomain>>
    suspend fun syncRemote(): Result<List<CryptoDomain>>
    suspend fun getDetails(id: String): Result<CryptoDomain>
}