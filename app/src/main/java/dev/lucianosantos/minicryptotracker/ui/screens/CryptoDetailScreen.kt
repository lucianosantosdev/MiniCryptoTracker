package dev.lucianosantos.minicryptotracker.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme
import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.ui.LocalSnackbarHostState
import dev.lucianosantos.minicryptotracker.utils.ObserveAsEvents
import dev.lucianosantos.minicryptotracker.utils.toUsdCurrencyString

@Composable
fun CryptoDetailScreen(
    cryptoId: String,
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalPlatformContext.current

    LaunchedEffect(cryptoId) {
        viewModel.fetchCryptoDetail(cryptoId)
    }
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
                    viewModel.fetchCryptoDetail(cryptoId)
                }
            }
        }
    }
    CryptoDetailScreenContent(
        isLoading = uiState.isLoading,
        crypto = uiState.selectedCrypto
    )
}

@Composable
fun CryptoDetailScreenContent(
    isLoading: Boolean,
    crypto: CryptoDomain? = null,
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    crypto?.let {
        val scrollState = rememberScrollState()
        Card(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            CryptoImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                imageUrl = it.imageUrl ?: ""
            )
            DetailItem(R.string.label_name, it.name)
            DetailItem(R.string.label_symbol, it.symbol)
            DetailItem(R.string.label_price, it.currentPrice?.toUsdCurrencyString() ?: stringResource(R.string.label_no_price))
            DetailItem(
                R.string.label_description,
                it.description.takeUnless { it.isNullOrEmpty() } ?: stringResource(R.string.label_no_description)
            )
        }
    }
}

@Composable
fun CryptoImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Box(
        modifier = modifier
            .size(96.dp)
            .background(color = Color.White, shape = CircleShape)
    ) {
        SubcomposeAsyncImage(
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(CircleShape),
            model = imageUrl,
            contentDescription = null,
            error = {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = stringResource(R.string.error_image_not_found),
                    modifier = Modifier.size(56.dp),
                    tint = Color.DarkGray
                )
            },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.padding(12.dp),
                    color = Color.LightGray
                )
            }
        )
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
        headlineContent = {
            Box(
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = value)
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@PreviewScreenSizes
fun CryptoDetailScreenPreview() {
    MiniCryptoTrackerTheme {
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(Color.Red.toArgb())
        }
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CryptoDetailScreenContent(
                crypto = CryptoDomain(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1696501400",
                    description = "Bitcoin is a decentralized digital currency.",
                    currentPrice = 50000.0
                ),
                isLoading = false
            )
        }
    }
}
