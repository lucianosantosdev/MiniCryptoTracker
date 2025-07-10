package dev.lucianosantos.minicryptotracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val cryptoViewModel = CryptoViewModel()
    val uiState by cryptoViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mini Crypto Tracker"
                    )
                },
                actions = {
                    if(uiState.currentRoute is Route.CryptoDetail) {
                        IconButton(onClick = {
                            cryptoViewModel.navigateTo(Route.CryptoList)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Navigate back to list"
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            when(uiState.currentRoute) {
                is Route.CryptoList -> {
                    CryptoListScreen(
                        cryptoViewModel = cryptoViewModel,
                        onCryptoItemClick = { cryptoItem ->
                            // Handle crypto item click, e.g., navigate to detail screen
                        }
                    )
                }
                is Route.CryptoDetail -> {
                    // Handle navigation to CryptoDetail screen
                    // For example, you could pass the cryptoId to a detail screen composable
                }
            }
        }
    }
}
