package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.LocalSnackbarHostState
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme
import dev.lucianosantos.minicryptotracker.utils.ObserveAsEvents

@Composable
fun CryptoListScreen(
    viewModel: CryptoViewModel,
    onCryptoItemClick: (CryptoDomain) -> Unit
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            is CryptoViewModel.UiEvent.ShowError -> {
                val errorMessage = event.message
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = errorMessage.asString(context),
                    actionLabel = context.getString(R.string.snackbar_action_retry),
                    duration = SnackbarDuration.Long
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    viewModel.fetchCryptoItems()
                }
            }
        }
    }
    CryptoListScreenContent(
        cryptoList = uiState.cryptoList,
        isLoading = uiState.isLoading,
        onRefresh = viewModel::fetchCryptoItems,
        onCryptoItemClick = onCryptoItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreenContent(
    cryptoList: List<CryptoDomain>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onCryptoItemClick: (CryptoDomain) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCryptoList = cryptoList.filter { crypto ->
        crypto.name.contains(searchQuery, ignoreCase = true) ||
        crypto.symbol.contains(searchQuery, ignoreCase = true)
    }
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query -> searchQuery = query }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredCryptoList) { cryptoItem ->
                    CryptoListItem(
                        cryptoDomain = cryptoItem,
                        onClick = onCryptoItemClick
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        },
        placeholder = { Text(text = stringResource(R.string.search_bar_hint)) }
    )
}
@Composable
fun CryptoListItem(
    cryptoDomain: CryptoDomain,
    onClick: (CryptoDomain) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable{
            onClick(cryptoDomain)
        },
        headlineContent = {
            Text(
                text = cryptoDomain.symbol
            )
        },
        supportingContent = {
            Text(
                text = cryptoDomain.name
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
            cryptoList = listOf(
                CryptoDomain(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://example.com/bitcoin.png",
                    description = "Bitcoin is a decentralized digital currency.",
                    currentPrice = 50000.0
                ),
                CryptoDomain(
                    id = "2",
                    name = "Ethereum",
                    symbol = "ETH",
                    imageUrl = "https://example.com/ethereum.png",
                    description = "Ethereum is a decentralized platform for smart contracts.",
                    currentPrice = 3000.0
                )
            ),
            isLoading = false,
            onCryptoItemClick = {},
            onRefresh = { }
        )
    }
}