package com.gps_g6.bestbiddertcg.feature.explore.presentation

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias de búsqueda en la pantalla Explore.
 *
 * Comprueba que la búsqueda filtra las subastas por el nombre de la carta.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreSearchTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuctionRepository
    private lateinit var viewModel: ExploreViewModel

    /**
     * Configura el entorno de prueba.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    /**
     * Limpia el entorno de prueba.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprueba que la búsqueda filtra por el nombre de la carta.
     */
    @Test
    fun `search filters auctions by card name`() = runTest(testDispatcher) {
        val auctions = listOf(
            createAuction("1", "Charizard", 50.0, 3, "IN_PROGRESS"),
            createAuction("2", "Pikachu", 20.0, 2, "UPCOMING"),
            createAuction("3", "Black Lotus", 100.0, 1, "IN_PROGRESS")
        )

        coEvery {
            repository.getAllAuctions(AuctionSort.ENDING_SOON)
        } returns auctions

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.applyFilters(query = "Charizard")

        assertEquals(
            listOf("Charizard"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que la búsqueda ignora mayúsculas y minúsculas.
     */
    @Test
    fun `search ignores uppercase and lowercase`() = runTest(testDispatcher) {
        val auctions = listOf(
            createAuction("1", "Charizard", 50.0, 3, "IN_PROGRESS")
        )

        coEvery {
            repository.getAllAuctions(AuctionSort.ENDING_SOON)
        } returns auctions

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.applyFilters(query = "CHARIZARD")

        assertEquals(1, viewModel.auctions.value.size)
    }

    /**
     * Comprueba que al borrar la búsqueda se recuperan todas las subastas.
     */
    @Test
    fun `clearing search restores all auctions`() = runTest(testDispatcher) {
        val auctions = listOf(
            createAuction("1", "Charizard", 50.0, 3, "IN_PROGRESS"),
            createAuction("2", "Pikachu", 20.0, 2, "UPCOMING")
        )

        coEvery {
            repository.getAllAuctions(AuctionSort.ENDING_SOON)
        } returns auctions

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.applyFilters(query = "Charizard")
        viewModel.applyFilters(query = "")

        assertEquals(2, viewModel.auctions.value.size)
    }

    /**
     * Crea una subasta de prueba.
     */
    private fun createAuction(
        id: String,
        name: String,
        price: Double,
        days: Int,
        status: String
    ): AuctionSummary {
        return AuctionSummary(
            id = id,
            cardName = name,
            cardImage = "",
            currentBid = price,
            remainingDays = days,
            cardState = "NEW",
            cardCategory = "Electric",
            cardCollection = "Pokemon",
            cardNumber = null,
            status = status
        )
    }
}
