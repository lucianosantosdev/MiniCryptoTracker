package dev.lucianosantos.minicryptotracker.ui

import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.model.AppError
import dev.lucianosantos.minicryptotracker.model.AppException
import dev.lucianosantos.minicryptotracker.utils.UiString

fun Throwable.toUiString(): UiString {
    return when (this) {
        is AppException -> when (error) {
            AppError.Network -> UiString.StringRes(R.string.error_network)
            AppError.BadRequest -> UiString.StringRes(R.string.error_bad_request)
            AppError.Unauthorized -> UiString.StringRes(R.string.error_unauthorized)
            AppError.Forbidden -> UiString.StringRes(R.string.error_forbidden)
            AppError.NotFound -> UiString.StringRes(R.string.error_not_found)
            AppError.Server -> UiString.StringRes(R.string.error_server)
            AppError.RateLimitExceeded -> UiString.StringRes(R.string.error_rate_limit_exceeded)
            AppError.UnexpectedResponse -> UiString.StringRes(R.string.error_unexpected_response)
            is AppError.Backend -> UiString.StringRes(R.string.error_backend, error.code, error.message)
            else -> UiString.StringRes(R.string.error_unknown)
        }
        else -> UiString.StringRes(R.string.error_unknown)
    }
}