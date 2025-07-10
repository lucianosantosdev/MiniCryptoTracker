package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import dev.lucianosantos.minicryptotracker.ui.CryptoItem
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel

@Composable
fun CryptoListScreen(
    cryptoViewModel: CryptoViewModel,
    onCryptoItemClick: (CryptoItem) -> Unit
) {
    val uiState = cryptoViewModel.uiState.collectAsState()
    CryptoListScreenContent(
        cryptoItems = uiState.value.cryptoItems,
        isLoading = uiState.value.isLoading,
        errorMessage = uiState.value.errorMessage,
        onRefresh = cryptoViewModel::fetchCryptoItems,
        onCryptoItemClick = onCryptoItemClick
    )
}

@Composable
fun CryptoListScreenContent(
    cryptoItems: List<CryptoItem>,
    isLoading: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit,
    onCryptoItemClick: (CryptoItem) -> Unit
) {
    LazyColumn {
        items(cryptoItems) { cryptoItem ->
            CryptoListItem(
                cryptoItem = cryptoItem,
                onClick = onCryptoItemClick
            )
        }
    }
}

@Composable
fun CryptoListItem(
    cryptoItem: CryptoItem,
    onClick: (CryptoItem) -> Unit
) {
    // This function would define how each cryptocurrency item is displayed
    // For example, it could show the name, symbol, and current price of the cryptocurrency
}

@Composable
@Preview(showBackground = true)
fun CryptoListScreenContentPreview() {
    CryptoListScreenContent(
        cryptoItems = listOf(
            CryptoItem(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://example.com/bitcoin.png",
                description = "Bitcoin is a decentralized digital currency.",
                currentPrice = 50000L
            ),
            CryptoItem(
                id = "2",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://example.com/ethereum.png",
                description = "Ethereum is a decentralized platform for smart contracts.",
                currentPrice = 3000L
            )
        ),
        isLoading = false,
        errorMessage = null,
        onCryptoItemClick = {},
        onRefresh = { }
    )
}