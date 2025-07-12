package dev.lucianosantos.minicryptotracker

import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.model.CryptoRepository
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CryptoViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher() // O
    private lateinit var cryptoRepository: CryptoRepository
    private lateinit var mockCryptoCoinsFlow: MutableStateFlow<List<CryptoDomain>>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cryptoRepository = mockk()
        mockCryptoCoinsFlow = MutableStateFlow(emptyList())
        every { cryptoRepository.cryptoCoins } returns mockCryptoCoinsFlow
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Should immediately fetch cached data`() {
        // Given
        val testCryptoList = listOf(
            CryptoDomain("1", "Bitcoin", "BTC", "1", "img1", 50000.0),
            CryptoDomain("2", "Ethereum", "ETH", "2", "img2", 3000.0)
        )
        mockCryptoCoinsFlow.value = testCryptoList
        // When
        val viewModel = CryptoViewModel(cryptoRepository)
        // Then
        assertEquals(testCryptoList, viewModel.uiState.value.cryptoList)
    }
}