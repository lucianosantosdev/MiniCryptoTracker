package dev.lucianosantos.minicryptotracker.ui

sealed class Route {
    object CryptoList: Route()
    object CryptoDetail: Route()
}