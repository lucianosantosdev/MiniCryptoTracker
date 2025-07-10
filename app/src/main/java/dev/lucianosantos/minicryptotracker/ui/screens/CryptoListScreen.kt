package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lucianosantos.minicryptotracker.ui.CryptoItem
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme

@Composable
fun CryptoListScreen(
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCryptoItems()
    }
    CryptoListScreenContent(
        cryptoItems = uiState.cryptoItems,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onRefresh = viewModel::fetchCryptoItems,
        onCryptoItemClick = viewModel::showCryptoDetail
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
    ListItem(
        modifier = Modifier.clickable{
            onClick(cryptoItem)
        },
        headlineContent = {
            Text(
                text = cryptoItem.symbol
            )
        },
        supportingContent = {
            Text(
                text = cryptoItem.name
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate to details",
            )
        },
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CryptoListScreenContentPreview() {
    MiniCryptoTrackerTheme {
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
}