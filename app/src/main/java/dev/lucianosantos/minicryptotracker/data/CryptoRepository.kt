package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.ui.CryptoItem
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    suspend fun fetchCryptoItems() : Result<List<CryptoItem>>
    suspend fun fetchCryptoItemById(id: String): Flow<CryptoItem>
}