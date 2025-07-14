package dev.lucianosantos.minicryptotracker.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

sealed class Route {
    object CryptoList: Route()
    data class CryptoDetail(
        val cryptoId: String
    ): Route()
}

val RouteSaver: Saver<Route, Any> = listSaver(
    save = { route ->
        when (route) {
            is Route.CryptoList -> listOf("CryptoList")
            is Route.CryptoDetail -> listOf("CryptoDetail", route.cryptoId)
        }
    },
    restore = { saved ->
        when (saved[0]) {
            "CryptoList" -> Route.CryptoList
            "CryptoDetail" -> Route.CryptoDetail(saved[1])
            else -> Route.CryptoList // fallback
        }
    }
)