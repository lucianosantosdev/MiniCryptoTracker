package dev.lucianosantos.minicryptotracker.model

import dev.lucianosantos.minicryptotracker.network.ErrorStatusDto
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class AppException(val error: AppError) : Exception()

sealed class AppError {
    data object Unknown : AppError()
    data object Network : AppError()
    data object BadRequest : AppError()
    data object Unauthorized : AppError()
    data object Forbidden : AppError()
    data object NotFound : AppError()
    data object Server : AppError()
    data object RateLimitExceeded : AppError()
    data object UnexpectedResponse : AppError()
    data class Backend(val code: Int, val message: String) : AppError()
}

fun HttpException.toAppError(): AppError {
    return when (code()) {
        400 -> AppError.BadRequest
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        500 -> AppError.Server
        else -> {
            response()?.toAppError() ?: AppError.Unknown
        }
    }
}

fun <T>  Response<T>.toAppError(): AppError {
    val errorBody = errorBody()


    return try {
        val errorDto = errorBody?.let {
            Json.decodeFromString<ErrorStatusDto>(errorBody.string())
        }
        val errorCode = errorDto?.status?.errorCode ?: code()
        return when (errorCode) {
            400 -> AppError.BadRequest
            401 -> AppError.Unauthorized
            403 -> AppError.Forbidden
            404 -> AppError.NotFound
            429 -> AppError.RateLimitExceeded
            500 -> AppError.Server
            else -> null
        } ?: errorDto?.let { it ->
            AppError.Backend(
                code = it.status?.errorCode ?: code(),
                message = it.status?.errorMessage ?: it.error ?: ""
            )
        } ?: AppError.UnexpectedResponse
    } catch (e: Exception) {
        AppError.Unknown
    }
}