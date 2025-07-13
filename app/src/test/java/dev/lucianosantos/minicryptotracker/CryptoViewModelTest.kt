package dev.lucianosantos.minicryptotracker

import app.cash.turbine.test
import dev.lucianosantos.minicryptotracker.model.CryptoDomain
import dev.lucianosantos.minicryptotracker.data.CryptoRepository
import dev.lucianosantos.minicryptotracker.ui.CryptoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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

    private lateinit var defaultTestViewModel: CryptoViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockCryptoRepository = mockk()
        mockCryptoCoinsFlow = MutableStateFlow(emptyList())
        every { mockCryptoRepository.cryptoCoins } returns mockCryptoCoinsFlow

        // Set default test view model
        val testCryptoList = listOf(
            CryptoDomain("1", "Bitcoin", "BTC", "1", "img1", 123.0),
            CryptoDomain("2", "Ethereum", "ETH", "2", "img2", 456.0)
        )
        mockCryptoCoinsFlow.value = testCryptoList
        defaultTestViewModel = CryptoViewModel(mockCryptoRepository)
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
        val viewModel = defaultTestViewModel
        // When
        coEvery { mockCryptoRepository.syncRemote() } returns Result.success(Unit)
        viewModel.fetchCryptoItems()
        // Then
        coVerify { mockCryptoRepository.syncRemote() }
    }

    @Test
    fun `Should update UI loading state when fetching remote data`() {
        // Given
        val viewModel = defaultTestViewModel
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should emit ShowError event when fetching remote data fails`() = runTest {
        // Given
        val viewModel = defaultTestViewModel
        val errorMessage = "Network error"
        coEvery { mockCryptoRepository.syncRemote() } returns Result.failure(Exception(errorMessage))

        viewModel.uiEvents.test {
            // When
            viewModel.fetchCryptoItems()
            // then
            val event = awaitItem()
            assert(event is CryptoViewModel.UiEvent.ShowError)
            assertEquals(errorMessage, (event as CryptoViewModel.UiEvent.ShowError).message)
        }
    }

    @Test
    fun `Should fetch crypto details when requested`() = runTest {
        // Given
        val viewModel = defaultTestViewModel
        val cryptoId = "1"
        val fakeIncompleteCrypto = CryptoDomain(cryptoId, "Bitcoin", "BTC")
        val fakeDetailedCrypto = CryptoDomain(
            id = cryptoId,
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://example.com/bitcoin.png",
            description = "Bitcoin is a decentralized digital currency.",
            currentPrice = 50000.0
        )
        coEvery { mockCryptoRepository.getDetails(cryptoId) } returns Result.success(fakeDetailedCrypto)

        // When
        viewModel.fetchCryptoDetail(fakeIncompleteCrypto)

        // Then
        assertEquals(fakeDetailedCrypto, viewModel.uiState.value.selectedCrypto)
    }

    @Test
    fun `Should emit ShowError event when fetching crypto details fails`() = runTest {
        // Given
        val viewModel = defaultTestViewModel
        val cryptoId = "1"
        val fakeIncompleteCrypto = CryptoDomain(cryptoId, "Bitcoin", "BTC")
        val errorMessage = "Details not found"
        coEvery { mockCryptoRepository.getDetails(cryptoId) } returns Result.failure(Exception(errorMessage))

        viewModel.uiEvents.test {
            // When
            viewModel.fetchCryptoDetail(fakeIncompleteCrypto)
            // Then
            val event = awaitItem()
            assert(event is CryptoViewModel.UiEvent.ShowError)
            assertEquals(errorMessage, (event as CryptoViewModel.UiEvent.ShowError).message)
        }
    }
}