package dev.lucianosantos.minicryptotracker.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorStatusDto(
    val status: Status
) {
    @Serializable
    data class Status(
        @SerialName("error_code") val errorCode: Int,
        @SerialName("error_message") val errorMessage: String
    )
}