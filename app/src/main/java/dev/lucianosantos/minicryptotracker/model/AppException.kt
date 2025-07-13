package dev.lucianosantos.minicryptotracker.model

import dev.lucianosantos.minicryptotracker.network.ErrorStatusDto
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException

class AppException(val error: AppError) : Exception()

sealed class AppError {
    data object Unknown : AppError()
    data object Network : AppError()
    data object BadRequest : AppError()
    data object Unauthorized : AppError()
    data object Forbidden : AppError()
    data object NotFound : AppError()
    data object Server : AppError()
    data object RateLimitExceeded: AppError()
    data object UnexpectedResponse : AppError()
    data class Backend(val code: Int, val message: String) : AppError()
}

fun HttpException.toAppError(): AppError {
    val errorBody = response()?.errorBody()
    return when (code()) {
        400 -> AppError.BadRequest
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        500 -> AppError.Server
        else -> {
            errorBody?.toAppError() ?: AppError.Unknown
        }
    }
}

fun ResponseBody.toAppError(): AppError {
    return try {
        val errorStatus = Json.decodeFromString<ErrorStatusDto>(string())
        return when (errorStatus.status.errorCode) {
            400 -> AppError.BadRequest
            401 -> AppError.Unauthorized
            403 -> AppError.Forbidden
            404 -> AppError.NotFound
            429 -> AppError.RateLimitExceeded
            500 -> AppError.Server
            else -> null
        } ?:
            AppError.Backend(
                code = errorStatus.status.errorCode,
                message = errorStatus.status.errorMessage
            )
    } catch (e: Exception) {
        AppError.Unknown
    }
}