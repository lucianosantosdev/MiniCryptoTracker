package dev.lucianosantos.minicryptotracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.lucianosantos.minicryptotracker.R
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoDetailScreen
import dev.lucianosantos.minicryptotracker.ui.screens.CryptoListScreen

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

sealed class Route {
    object CryptoList: Route()
    data class CryptoDetail(
        val cryptoId: String
    ): Route()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: CryptoViewModel
) {
    var currentRoute: Route by remember { mutableStateOf(Route.CryptoList) }
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
                            text = stringResource(R.string.app_name),
                        )
                    },
                    navigationIcon = {
                        if(currentRoute is Route.CryptoDetail) {
                            IconButton(onClick = {
                                currentRoute = Route.CryptoList
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    actions = {
                        if(currentRoute is Route.CryptoDetail) {
                            IconButton(onClick = {
                                viewModel.fetchCryptoDetail(
                                    cryptoId = (currentRoute as Route.CryptoDetail).cryptoId
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
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
                when(currentRoute) {
                    is Route.CryptoList -> {
                        CryptoListScreen(
                            viewModel = viewModel,
                            onCryptoItemClick = { crypto ->
                                currentRoute = Route.CryptoDetail(cryptoId = crypto.id)
                            }
                        )
                    }
                    is Route.CryptoDetail -> {
                        CryptoDetailScreen(
                            cryptoId = (currentRoute as Route.CryptoDetail).cryptoId,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
