package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme

@Composable
fun CryptoDetailScreen(
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    CryptoDetailScreenContent(
        name = uiState.selectedCrypto?.name ?: "Unknown",
        symbol = uiState.selectedCrypto?.symbol ?: "Unknown",
        imageUrl = uiState.selectedCrypto?.imageUrl ?: "",
        description = uiState.selectedCrypto?.description ?: "No description available",
        currentPrice = uiState.selectedCrypto?.currentPrice ?: 0L
    )
}

@Composable
fun CryptoDetailScreenContent(
    name: String,
    symbol: String,
    imageUrl: String,
    description: String,
    currentPrice: Long
) {
    Text(
        text = "Crypto Detail Screen"
    )
}

@Composable
@Preview(showSystemUi = true)
fun CryptoDetailScreenPreview() {
    MiniCryptoTrackerTheme {
        CryptoDetailScreenContent(
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://example.com/bitcoin.png",
            description = "Bitcoin is a decentralized digital currency.",
            currentPrice = 50000L
        )
    }
}
