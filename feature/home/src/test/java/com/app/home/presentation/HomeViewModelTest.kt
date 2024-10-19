package com.app.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.app.core.utils.DataState
import com.app.domain.model.Banner
import com.app.domain.model.CardData
import com.app.domain.repository.Repository
import com.app.domain.usecase.GetBannerListUseCase
import com.app.domain.usecase.GetCardListUseCase
import com.app.home.utils.DispatcherProvider
import com.app.home.utils.MainDispatcherRule
import com.app.home.utils.TestDispatcherProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    private lateinit var useCase: GetCardListUseCase

    @get:Rule
    var testInstantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var bannerListUseCase: GetBannerListUseCase

   /* @Mock
    lateinit var repository: Repository*/

    //private lateinit var dispatcherProvider: DispatcherProvider


    @Before
    fun setUp() {
        //dispatcherProvider = TestDispatcherProvider()
        //MockitoAnnotations.openMocks(this)
        //useCase = Mockito.mock(GetCardListUseCase::class.java)
     /*   bannerListUseCase = Mockito.mock(GetBannerListUseCase::class.java)
        dispatcherProvider = Mockito.mock(DispatcherProvider::class.java)*/

        // Mock the dispatcherProvider to use the UnconfinedTestDispatcher
     /*   Mockito.`when`(dispatcherProvider.io).thenReturn(UnconfinedTestDispatcher())
        Mockito.`when`(dispatcherProvider.main).thenReturn(UnconfinedTestDispatcher())
*/

    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() = runTest {
            doReturn(flow {
                emit(DataState.Loading)
                emit(DataState.Success(testInputCards))
            }).`when`(useCase).invoke()
        //doReturn(flowOf(emptyList<Banner>())).`when`(bannerListUseCase).invoke()
        //useCase = GetCardListUseCase(repository)

        val viewModel = HomeViewModel(useCase, bannerListUseCase)
            viewModel.uiState.test {
                assertEquals(
                    CardListUiState.Success(testInputCards, emptyList()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(useCase).invoke()

 /*       val fakeCardList = listOf( CardData(
            id = "hgss4-1",
            thumbnail = "https://images.pokemontcg.io/hgss4/1.png",
            image = "https://images.pokemontcg.io/hgss4/1_hires.png"
        )
        )

        `when`(useCase.getCards()).thenReturn(
            flow {
                emit(DataState.Loading)
                emit(DataState.Success(fakeCardList))
            }
        )

        // Act
        viewModel.getCardData()

        // Assert
        viewModel.uiState.test {
            // First emission should be Loading
            assertEquals(CardListUiState.Loading, awaitItem())

            // Second emission should be Success with the card list
            assertEquals(CardListUiState.Success(fakeCardList, emptyList()), awaitItem())

            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }*/
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() =
        runTest {
            val errorMessage = "Error Message For You"
            val exception = IllegalStateException(errorMessage)
            doReturn(flow<List<CardData>> {
                throw exception
            }).`when`(useCase).invoke()
            val viewModel = HomeViewModel(useCase, bannerListUseCase)

            viewModel.uiState.test {
                assertEquals(
                    CardListUiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(useCase).invoke()
        }


    @After
    fun tearDown() {
        // do something if required
    }

    private val testInputCards = listOf(
        CardData(
                id = "hgss4-1",
                thumbnail = "https://images.pokemontcg.io/hgss4/1.png",
                image = "https://images.pokemontcg.io/hgss4/1_hires.png"
            ),
        )

}