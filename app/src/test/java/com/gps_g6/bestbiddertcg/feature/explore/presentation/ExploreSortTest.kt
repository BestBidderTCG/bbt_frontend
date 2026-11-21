package com.gps_g6.bestbiddertcg.feature.explore.presentation

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
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
 * Pruebas unitarias de los criterios de ordenación de Explore.
 *
 * Comprueba que el ViewModel envía al repositorio el criterio seleccionado
 * y que POPULAR utiliza ENDING_SOON como criterio por defecto.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreSortTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuctionRepository
    private lateinit var viewModel: ExploreViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprueba que todos los criterios disponibles se envían al repositorio.
     */
    @Test
    fun `all sort options are sent to repository`() = runTest(testDispatcher) {
        val requestedSorts = mutableListOf<AuctionSort>()

        coEvery {
            repository.getAllAuctions(capture(requestedSorts))
        } returns emptyList()

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        val sortOptions = AuctionSort.values()
            .filter { it != AuctionSort.POPULAR }

        sortOptions.forEach { sort ->
            viewModel.changeSort(sort)
            advanceUntilIdle()
        }

        val expectedSorts = listOf(AuctionSort.ENDING_SOON) + sortOptions

        assertEquals(expectedSorts, requestedSorts)
    }

    /**
     * Comprueba que POPULAR utiliza ENDING_SOON en la consulta.
     */
    @Test
    fun `popular sort uses ending soon in repository`() = runTest(testDispatcher) {
        val requestedSorts = mutableListOf<AuctionSort>()

        coEvery {
            repository.getAllAuctions(capture(requestedSorts))
        } returns emptyList()

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.changeSort(AuctionSort.PRICE_ASC)
        advanceUntilIdle()

        viewModel.changeSort(AuctionSort.POPULAR)
        advanceUntilIdle()

        assertEquals(
            listOf(
                AuctionSort.ENDING_SOON,
                AuctionSort.PRICE_ASC,
                AuctionSort.ENDING_SOON
            ),
            requestedSorts
        )

        assertEquals(AuctionSort.POPULAR, viewModel.sort.value)
    }

    /**
     * Comprueba que seleccionar el criterio actual no genera otra consulta.
     */
    @Test
    fun `selecting current sort does not reload auctions`() = runTest(testDispatcher) {
        val requestedSorts = mutableListOf<AuctionSort>()

        coEvery {
            repository.getAllAuctions(capture(requestedSorts))
        } returns emptyList()

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.changeSort(AuctionSort.POPULAR)
        advanceUntilIdle()

        assertEquals(
            listOf(AuctionSort.ENDING_SOON),
            requestedSorts
        )
    }
}