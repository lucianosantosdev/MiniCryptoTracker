package dev.lucianosantos.minicryptotracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoDetailScreen
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoListScreen
import dev.lucianosantos.minicryptotracker.data.CryptoRepositoryImpl
import dev.lucianosantos.minicryptotracker.database.CryptoDatabase
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = {
                        Text(
                            text = "Mini Crypto Tracker"
                        )
                    },
                    navigationIcon = {
                        if(uiState.currentRoute is Route.CryptoDetail) {
                            IconButton(onClick = {
                                viewModel.navigateTo(Route.CryptoList)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "No navigation",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    actions = {
                        if(uiState.currentRoute is Route.CryptoDetail) {
                            IconButton(onClick = {
                                viewModel.fetchCryptoDetail(
                                    uiState.selectedCrypto!!
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Navigate back to list",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                when(uiState.currentRoute) {
                    is Route.CryptoList -> {
                        CryptoListScreen(
                            viewModel = viewModel
                        )
                    }
                    is Route.CryptoDetail -> {
                        CryptoDetailScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
