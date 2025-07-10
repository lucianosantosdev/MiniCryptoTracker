package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme
import dev.lucianosantos.minicryptotracker.R

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
    Card {
        DetailItem(R.string.label_name, name)
        DetailItem(R.string.label_symbol, symbol)
        DetailItem(R.string.label_image_url, imageUrl)
        DetailItem(R.string.label_price, currentPrice.toString())
        DetailItem(R.string.label_description, description)
    }
}

@Composable
fun DetailItem(
    @StringRes label: Int,
    value: String
) {
    ListItem(
        modifier = Modifier.padding(16.dp),
        overlineContent = { Text(text = stringResource(label)) },
        headlineContent = { Text(text = value) }
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
