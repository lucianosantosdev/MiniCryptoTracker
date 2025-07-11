package dev.lucianosantos.minicryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.lucianosantos.minicryptotracker.data.CryptoRepositoryImpl
import dev.lucianosantos.minicryptotracker.database.CryptoDatabase
import dev.lucianosantos.minicryptotracker.network.CoinGeckoAPI
import dev.lucianosantos.minicryptotracker.ui.App
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(this)
                .apply {
                    if (BuildConfig.DEBUG) {
                        logger(DebugLogger())
                    }
                }
                .crossfade(true)
                .build()
        }

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val database = Room.databaseBuilder(
                context = context,
                klass = CryptoDatabase::class.java,
                name = "crypto_database.db"
            ).build()
            val repository = CryptoRepositoryImpl(
                cryptoDao = database.cryptoDao(),
                coinGeckoAPI = CoinGeckoAPI.create()
            )
            val cryptoViewModel = CryptoViewModel(
                repository
            )
            MiniCryptoTrackerTheme {
                App(cryptoViewModel)
            }
        }
    }
}