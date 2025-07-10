package dev.lucianosantos.minicryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lucianosantos.minicryptotracker.data.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val cryptoRepository: CryptoRepository

) : ViewModel() {
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
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            val items = cryptoRepository.fetchCryptoItems()
            _uiState.update {
                it.copy(
                    cryptoItems = items.map {
                        CryptoItem(
                            id = it.id,
                            name = it.name,
                            symbol = it.symbol,
                            imageUrl = it.imageUrl,
                            description = it.description,
                            currentPrice = it.currentPrice
                        )
                    },
                    isLoading = false
                )
            }
        }
    }

    fun refreshCryptoItems() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
        fetchCryptoItems()
    }
    fun showCryptoDetail(cryptoItem: CryptoItem) {
        _uiState.update {
            it.copy(selectedCrypto = cryptoItem, currentRoute = Route.CryptoDetail(cryptoItem.id))
        }
    }

    fun navigateTo(route: Route) {
        _uiState.update {
            it.copy(currentRoute = route)
        }
    }


}