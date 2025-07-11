package dev.lucianosantos.minicryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lucianosantos.minicryptotracker.data.CryptoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val cryptoRepository: CryptoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    data class UiState(
        val cryptoDomains: List<CryptoDomain> = emptyList(),
        val isLoading: Boolean = false,
        val currentRoute: Route = Route.CryptoList,
        val selectedCrypto: CryptoDomain? = null
    )

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
    }

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
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _uiEvents.trySend(
            UiEvent.ShowError(items.exceptionOrNull()?.message ?: "Unknown error")
                )
            }
        }
    }

    fun refreshCryptoItems() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        fetchCryptoItems()
    }

    fun fetchCryptoDetail(cryptoDomain: CryptoDomain) {
        navigateTo(Route.CryptoDetail)
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = cryptoRepository.getDetails(cryptoDomain.id)
            if (result.isSuccess) {
                val cryptoDetails = result.getOrNull()
                if (cryptoDetails != null) {
                    _uiState.update {
                        it.copy(
                            selectedCrypto = cryptoDetails
                        )
                    }
                } else {
                    _uiEvents.trySend(UiEvent.ShowError("Crypto details not found"))
                }
            } else {
                _uiEvents.trySend(UiEvent.ShowError(result.exceptionOrNull()?.message ?: "Unknown error"))
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }


    fun navigateTo(route: Route) {
        _uiState.update {
            it.copy(currentRoute = route)
        }
    }
}