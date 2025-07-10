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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoDetailScreen
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoListScreen
import dev.lucianosantos.minicryptotracker.data.CryptoRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val repository = CryptoRepositoryImpl()
    val cryptoViewModel = CryptoViewModel(
        repository
    )
    val uiState by cryptoViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                            cryptoViewModel.navigateTo(Route.CryptoList)
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
                    if(uiState.currentRoute is Route.CryptoList) {
                        IconButton(onClick = {
                            cryptoViewModel.refreshCryptoItems()
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
                        viewModel = cryptoViewModel
                    )
                }
                is Route.CryptoDetail -> {
                    CryptoDetailScreen(
                        viewModel = cryptoViewModel
                    )
                }
            }
        }
    }
}
