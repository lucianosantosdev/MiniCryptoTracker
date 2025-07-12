package dev.lucianosantos.minicryptotracker

import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.model.CryptoRepository
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
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
    private lateinit var mockCryptoRepository: CryptoRepository
    private lateinit var mockCryptoCoinsFlow: MutableStateFlow<List<CryptoDomain>>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockCryptoRepository = mockk()
        mockCryptoCoinsFlow = MutableStateFlow(emptyList())
        every { mockCryptoRepository.cryptoCoins } returns mockCryptoCoinsFlow
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
            CryptoDomain("1", "Bitcoin", "BTC", "1", "img1", 123.0),
            CryptoDomain("2", "Ethereum", "ETH", "2", "img2", 456.0)
        )
        mockCryptoCoinsFlow.value = testCryptoList
        // When
        val viewModel = CryptoViewModel(mockCryptoRepository)
        // Then
        assertEquals(testCryptoList, viewModel.uiState.value.cryptoList)
    }

    @Test
    fun `Should fetch remote data when cache is empty`() {
        // Given
        mockCryptoCoinsFlow.value = emptyList()
        coEvery { mockCryptoRepository.syncRemote() } returns Result.success(Unit)
        // When
        CryptoViewModel(mockCryptoRepository)
        // Then
        coVerify { mockCryptoRepository.syncRemote() }
    }

    @Test
    fun `Should not fetch remote data when cache is not empty`() {
        // Given
        val testCryptoList = listOf(
            CryptoDomain("1", "Bitcoin", "BTC", "1", "img1", 123.0),
            CryptoDomain("2", "Ethereum", "ETH", "2", "img2", 456.0)
        )
        mockCryptoCoinsFlow.value = testCryptoList
        // When
        CryptoViewModel(mockCryptoRepository)
        // Then
        coVerify(exactly = 0) { mockCryptoRepository.syncRemote() }
    }

    @Test
    fun `Should fetch remote data when user requests it`() {
        // Given
        val viewModel = CryptoViewModel(mockCryptoRepository)
        // When
        coEvery { mockCryptoRepository.syncRemote() } returns Result.success(Unit)
        viewModel.fetchCryptoItems()
        // Then
        coVerify { mockCryptoRepository.syncRemote() }
    }

    @Test
    fun `Should update UI loading state when fetching remote data`() {
        // Given
        val viewModel = CryptoViewModel(mockCryptoRepository)
        coEvery { mockCryptoRepository.syncRemote() } coAnswers {
            assertEquals(true, viewModel.uiState.value.isLoading)
            Result.success(Unit)
        }
        // When
        viewModel.fetchCryptoItems()
        // Then
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Should update UI state with fetched data`() {
        // Given
        val viewModel = CryptoViewModel(mockCryptoRepository)
        // When
        val testCryptoList = listOf(
            CryptoDomain("1", "Bitcoin", "BTC", "1", "img1", 50000.0),
            CryptoDomain("2", "Ethereum", "ETH", "2", "img2", 3000.0)
        )
        mockCryptoCoinsFlow.value = testCryptoList
        // Then
        assertEquals(testCryptoList, viewModel.uiState.value.cryptoList)
    }
}