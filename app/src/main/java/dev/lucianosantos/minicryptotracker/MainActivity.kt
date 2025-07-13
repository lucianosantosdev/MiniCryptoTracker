package dev.lucianosantos.minicryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.lucianosantos.minicryptotracker.data.CryptoRepositoryImpl
import dev.lucianosantos.minicryptotracker.database.CryptoDatabase
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import dev.lucianosantos.minicryptotracker.ui.App
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModelFactory: CryptoViewModel.CryptoViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .apply {
                    if (BuildConfig.DEBUG) {
                        logger(DebugLogger())
                    }
                }
                .crossfade(true)
                .build()
        }
        val database = CryptoDatabase.create(applicationContext)
        val repository = CryptoRepositoryImpl(
            cryptoDao = database.cryptoDao(),
            coinGeckoAPI = CoinGeckoAPI.create()
        )
        viewModelFactory = CryptoViewModel.CryptoViewModelFactory(
            repository = repository
        )
        enableEdgeToEdge()
        setContent {
            MiniCryptoTrackerTheme {
                val cryptoViewModel: CryptoViewModel = viewModel(factory = viewModelFactory)
                App(cryptoViewModel)
            }
        }
    }
}