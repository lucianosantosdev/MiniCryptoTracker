package dev.lucianosantos.minicryptotracker.ui.screens

import android.icu.text.NumberFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme
import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.utils.toUsdCurrencyString
import java.util.Locale

@Composable
fun CryptoDetailScreen(
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    CryptoDetailScreenContent(
        isLoading = uiState.isLoading,
        name = uiState.selectedCrypto?.name ?: "Unknown",
        symbol = uiState.selectedCrypto?.symbol ?: "Unknown",
        imageUrl = uiState.selectedCrypto?.imageUrl ?: "",
        description = uiState.selectedCrypto?.description ?: "No description available",
        currentPrice = uiState.selectedCrypto?.currentPrice ?: 0.0
    )
}

@Composable
fun CryptoDetailScreenContent(
    isLoading: Boolean,
    name: String,
    symbol: String,
    imageUrl: String,
    description: String,
    currentPrice: Double
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
    Card {
        CryptoImage(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            imageUrl = imageUrl
        )
        DetailItem(R.string.label_name, name)
        DetailItem(R.string.label_symbol, symbol)
        DetailItem(R.string.label_price, currentPrice.toUsdCurrencyString())
        DetailItem(R.string.label_description, description)
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
    val scrollState = rememberScrollState()
    ListItem(
        modifier = Modifier.padding(16.dp),
        overlineContent = { Text(text = stringResource(label)) },
        headlineContent = {
            Box(
                modifier = Modifier
                    .heightIn(max = 200.dp)
                    .verticalScroll(scrollState)
                    .padding(end = 8.dp)
            ) {
                Text(text = value)
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview(showSystemUi = true)
fun CryptoDetailScreenPreview() {
    MiniCryptoTrackerTheme {
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(Color.Red.toArgb())
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            CryptoDetailScreenContent(
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1696501400",
                description = "Bitcoin is a decentralized digital currency.",
                currentPrice = 50000.0,
                isLoading = false
            )
        }
    }
}
