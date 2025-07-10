package dev.lucianosantos.minicryptotracker.data

import dev.lucianosantos.minicryptotracker.BuildConfig
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import dev.lucianosantos.minicryptotracker.ui.CryptoItem
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class CryptoRepositoryImpl: CryptoRepository {
    private val coinApi: CoinGeckoAPI by lazy {
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", BuildConfig.COINGECKO_API_KEY) // or the correct header name expected by the API
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CoinGeckoAPI::class.java)
    }

    override suspend fun fetchCryptoItems(): Result<List<CryptoItem>> {
        return try {
            val response = coinApi.getCoinsList()
            if (response.isSuccessful) {
                val items = response.body()?.map { coin ->
                    CryptoItem(
                        id = coin.id,
                        name = coin.name,
                        symbol = coin.symbol,
                        imageUrl = "",
                        description = "",
                        currentPrice = 0L
                    )
                } ?: emptyList()
                Result.success(items)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Server error: ${response.code()} - ${errorBody ?: "Unknown"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchCryptoItemById(id: String): Flow<CryptoItem> {
        TODO("Not yet implemented")
    }
}