package dev.lucianosantos.minicryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.data.CryptoRepository
import dev.lucianosantos.minicryptotracker.model.AppError
import dev.lucianosantos.minicryptotracker.model.AppException
import dev.lucianosantos.minicryptotracker.utils.UiString
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
        val cryptoList: List<CryptoDomain> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCrypto: CryptoDomain? = null
    )

    sealed class UiEvent {
        data class ShowError(val message: UiString) : UiEvent()
    }

    init {
        viewModelScope.launch {
            cryptoRepository.cryptoCoins.collect { items ->
                if (items.isEmpty()) {
                    fetchCryptoItems()
                } else {
                    _uiState.update {
                        it.copy(
                            cryptoList = items
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
            if (items.isFailure) {
               val error = items.exceptionOrNull() ?: AppException(AppError.Unknown)
               val appError = error.toUiString()
                _uiEvents.trySend(UiEvent.ShowError(appError))
            }
            _uiState.update { it.copy(isLoading = false)}
        }
    }

    fun fetchCryptoDetail(cryptoId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = cryptoRepository.getDetails(cryptoId)
            if (result.isSuccess) {
                val cryptoDetails = result.getOrNull()
                if (cryptoDetails != null) {
                    _uiState.update {
                        it.copy(
                            selectedCrypto = cryptoDetails
                        )
                    }
                } else {
                    _uiEvents.trySend(UiEvent.ShowError(UiString.StringRes(R.string.error_detail_not_found)))
                }
            } else {
                val error = result.exceptionOrNull() ?: AppException(AppError.Unknown)
                val appError = error.toUiString()
                _uiEvents.trySend(UiEvent.ShowError(appError))
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}
