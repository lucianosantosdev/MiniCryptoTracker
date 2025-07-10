package dev.lucianosantos.minicryptotracker.network

import retrofit2.Response
import retrofit2.http.GET

interface CoinGeckoAPI {
@GET("coins/list")
suspend fun getCoinsList() : Response<List<Coin>>
}