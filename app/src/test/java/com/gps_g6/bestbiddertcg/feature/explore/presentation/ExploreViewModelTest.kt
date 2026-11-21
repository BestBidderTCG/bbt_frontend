package com.gps_g6.bestbiddertcg.feature.explore.presentation

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias del ViewModel de la pantalla Explore.
 *
 * Comprueba que las subastas creadas pueden recuperarse y mostrarse
 * correctamente en el listado de subastas activas.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {

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
     * Comprueba que una subasta creada aparece en el listado de Explore.
     *
     * Simula una primera consulta sin resultados, la creación de una subasta
     * y una segunda consulta que devuelve la subasta creada.
     *
     * Verifica que el ViewModel actualiza correctamente la lista.
     */
    @Test
    fun `created auction is displayed in explore list`() =
        runTest(testDispatcher) {

            // Simula una subasta creada
            val createdAuction = Auction(
                id = "auction-1",
                creatorId = "user-1",
                creatorUsername = "testuser",
                card = "Pikachu",
                category = CardCategory.POKEMON.apiValue,
                collection = CardCollection.POKEMON_151.apiValue,
                condition = CardCondition.NEW,
                minPrize = 10.0,
                startedAt = "2026-11-06T12:00:00Z",
                durationInDays = 3,
                endsAt = "2026-11-09T12:00:00Z",
                status = AuctionStatus.ACTIVE
            )

            // Resumen con el que aparece esa subasta en el listado.
            val createdSummary = AuctionSummary(
                id = "auction-1",
                cardName = "Pikachu",
                cardImage = "",
                currentBid = 10.0,
                remainingDays = 3,
                cardState = CardCondition.NEW,
                status = AuctionStatus.ACTIVE,
                cardCategory = CardCategory.POKEMON,
                cardCollection = CardCollection.POKEMON_151,
                cardNumber = null
            )

            // Prepara los datos de la nueva subasta.
            val request = CreateAuction(
                creatorId = "user-1",
                creatorUsername = "testuser",
                cardId = "card-1",
                description = "Test auction",
                minPrize = 10.0,
                startedAt = "2026-11-06T12:00:00Z",
                durationInDays = 3
            )

            // Configura las respuestas del repositorio para simular la carga inicial y posterior.
            coEvery {
                repository.getAllAuctions(AuctionSort.ENDING_SOON)
            } returns emptyList() andThen listOf(createdSummary)

            // Configura la respuesta del repositorio al crear la subasta.
            coEvery {
                repository.createAuction(request)
            } returns createdAuction

            // Crea el ViewModel después de configurar el repositorio.
            viewModel = ExploreViewModel(repository)

            // Completa la carga inicial para comprobar la primera respuesta vacía.
            advanceUntilIdle()
            assertTrue(viewModel.auctions.value.isEmpty())

            // Ejecuta el caso de uso para crear la subasta.
            CreateAuctionUseCase(repository)(request)

            // Actualiza el listado después de crear la subasta.
            viewModel.loadAuctions()
            advanceUntilIdle()

            // Comprueba que la subasta creada aparece en el listado.
            assertEquals(
                listOf(createdSummary),
                viewModel.auctions.value
            )

            // Comprueba que la carga ha finalizado sin errores.
            assertFalse(viewModel.isLoading.value)
            assertNull(viewModel.error.value)

            // Verifica que la creación y las consultas al repositorio se han ejecutado correctamente.
            coVerify(exactly = 1) {
                repository.createAuction(request)
            }

            coVerify(exactly = 2) {
                repository.getAllAuctions(AuctionSort.ENDING_SOON)
            }
        }

    /**
     * Comprueba que el listado de Explore gestiona correctamente una respuesta vacía.
     *
     * Verifica que el ViewModel mantiene la lista vacía y no muestra ningún error.
     */
    @Test
    fun `empty auction list is handled correctly`() = runTest(testDispatcher) {
        // Simula una respuesta del repositorio sin subastas.
        coEvery { repository.getAllAuctions(AuctionSort.ENDING_SOON) } returns emptyList()

        // Crea el ViewModel después de configurar el repositorio simulado.
        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        // Comprueba que el listado está vacío.
        assertTrue(viewModel.auctions.value.isEmpty())

        // Comprueba que la carga ha finalizado sin errores.
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)

        // Verifica que se ha consultado el repositorio una vez.
        coVerify(exactly = 1) { repository.getAllAuctions(AuctionSort.ENDING_SOON) }
    }

    /**
     * Comprueba que el ViewModel gestiona correctamente un error al cargar las subastas.
     *
     * Verifica que se muestra el mensaje de error y que la carga finaliza.
     */
    @Test
    fun `auction loading error is handled correctly`() = runTest(testDispatcher) {
        // Simula un error al consultar el repositorio.
        coEvery {
            repository.getAllAuctions(AuctionSort.ENDING_SOON)
        } throws RuntimeException("Connection error")

        // Crea el ViewModel después de configurar el repositorio simulado.
        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        // Comprueba que no se han cargado subastas.
        assertTrue(viewModel.auctions.value.isEmpty())

        // Comprueba que la carga ha finalizado.
        assertFalse(viewModel.isLoading.value)

        // Comprueba que se ha publicado el mensaje de error esperado.
        assertEquals(
            "No se han podido cargar las subastas.",
            viewModel.error.value
        )

        // Verifica que se ha consultado el repositorio una vez.
        coVerify(exactly = 1) { repository.getAllAuctions(AuctionSort.ENDING_SOON) }
    }

    /**
     * Comprueba que al cambiar la ordenación se mantienen los filtros activos.
     *
     * Aplica un filtro de colección, cambia el orden y verifica que la lista
     * sigue filtrada con el orden nuevo.
     */
    @Test
    fun `changing sort keeps active filters`() = runTest(testDispatcher) {
        val pokemon = AuctionSummary(
            id = "1",
            cardName = "Pikachu",
            cardImage = "",
            currentBid = 10.0,
            remainingDays = 3,
            cardState = CardCondition.NEW,
            cardCategory = CardCategory.POKEMON,
            cardCollection = CardCollection.POKEMON_151,
            status = AuctionStatus.ACTIVE,
            cardNumber = null
        )
        val magic = pokemon.copy(
            id = "2",
            cardName = "Bolt",
            cardCategory = CardCategory.MAGIC,
            cardCollection = CardCollection.CICLO_DE_URZA
        )

        coEvery { repository.getAllAuctions(AuctionSort.ENDING_SOON) } returns listOf(pokemon, magic)
        coEvery { repository.getAllAuctions(AuctionSort.PRICE_ASC) } returns listOf(magic, pokemon)

        viewModel = ExploreViewModel(repository)
        advanceUntilIdle()

        viewModel.applyFilters(collection = CardCollection.POKEMON_151.apiValue)
        viewModel.changeSort(AuctionSort.PRICE_ASC)
        advanceUntilIdle()

        assertEquals(listOf(pokemon), viewModel.auctions.value)
        coVerify(exactly = 1) { repository.getAllAuctions(AuctionSort.PRICE_ASC) }
    }
}