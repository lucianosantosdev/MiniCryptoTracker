package dev.lucianosantos.minicryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lucianosantos.minicryptotracker.data.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val cryptoRepository: CryptoRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val cryptoDomains: List<CryptoDomain> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val currentRoute: Route = Route.CryptoList,
        val selectedCrypto: CryptoDomain? = null
    )

    init {
        viewModelScope.launch {
            cryptoRepository.cryptoCoins.collect { items ->
                if (items.isEmpty()) {
                    fetchCryptoItems()
                } else {
                    _uiState.update {
                        it.copy(
                            cryptoDomains = items
                        )
                    }
                }
            }
        }
    }

    fun fetchCryptoItems() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            val items = cryptoRepository.syncRemote()
            if (items.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        cryptoDomains = items.getOrNull() ?: emptyList()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = items.exceptionOrNull()?.message ?: "Unknown error"
                    )
                }
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

    fun showCryptoDetail(cryptoDomain: CryptoDomain) {
        _uiState.update {
            it.copy(selectedCrypto = cryptoDomain, currentRoute = Route.CryptoDetail(cryptoDomain.id))
        }
    }

    fun navigateTo(route: Route) {
        _uiState.update {
            it.copy(currentRoute = route)
        }
    }


}