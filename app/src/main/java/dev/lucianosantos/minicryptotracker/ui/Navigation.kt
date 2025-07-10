package dev.lucianosantos.minicryptotracker.ui

sealed class Route {
    object CryptoList: Route()
    data class CryptoDetail(
        val cryptoId: String
    ) : Route()
}