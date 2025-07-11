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
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.lucianosantos.minicryptotracker.ui.App
import dev.lucianosantos.minicryptotracker.ui.theme.MiniCryptoTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val imageLoader = ImageLoader.Builder(this)
            .logger(DebugLogger()) // <- Enables debug logging
            .build()

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .logger(DebugLogger()) // <- Enables debug logging
                .crossfade(true)
                .build()
        }

        enableEdgeToEdge()
        setContent {
            MiniCryptoTrackerTheme {
                App()
            }
        }
    }
}