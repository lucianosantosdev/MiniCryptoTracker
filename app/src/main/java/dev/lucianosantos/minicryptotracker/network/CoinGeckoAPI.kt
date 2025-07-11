package dev.lucianosantos.minicryptotracker.network

import dev.lucianosantos.minicryptotracker.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

interface CoinGeckoAPI {
    @GET("coins/list")
    suspend fun getCoinsList(): Response<List<CoinDto>>

    companion object {
        fun create(): CoinGeckoAPI {
            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-KEY", BuildConfig.COINGECKO_API_KEY)
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build()

            val json = Json {
                ignoreUnknownKeys = true
            }
            return Retrofit.Builder()
                    .baseUrl("https://api.coingecko.com/api/v3/")
                    .client(client)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(CoinGeckoAPI::class.java)

        }
    }

}