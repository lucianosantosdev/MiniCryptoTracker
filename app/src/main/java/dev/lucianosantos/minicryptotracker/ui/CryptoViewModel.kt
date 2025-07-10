package dev.lucianosantos.minicryptotracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CryptoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val cryptoItems: List<CryptoItem> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val currentRoute: Route = Route.CryptoList,
        val selectedCrypto: CryptoItem? = null
    )

    fun fetchCryptoItems() {
        // This function would contain the logic to fetch cryptocurrency items
        // For example, it could make a network request to an API and update _uiState
        // with the fetched data or an error message.
    }

    fun navigateTo(route: Route) {
        _uiState.update {
            it.copy(currentRoute = route)
        }
    }


}