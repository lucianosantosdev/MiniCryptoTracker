package dev.lucianosantos.minicryptotracker.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiString {
    class StringRes(
        @androidx.annotation.StringRes val resId: Int,
        vararg val args: Any
    ) : UiString()
    data class StringRaw(val value: String) : UiString()

    @Composable
    fun asString(): String {
        return when (this) {
            is StringRes -> stringResource(resId, *args)
            is StringRaw -> value
        }
    }

    fun asString(context: android.content.Context): String {
        return when (this) {
            is StringRes -> context.getString(resId, *args)
            is StringRaw -> value
        }
    }
}