package com.app.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.app.core.utils.DataState
import com.app.domain.model.CardData
import com.app.domain.repository.Repository
import com.app.domain.usecase.GetBannerListUseCase
import com.app.domain.usecase.GetCardListUseCase
import com.app.home.utils.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
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

    @Mock
    private lateinit var repository: Repository


    @Before
    fun setUp() {
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() = runTest {
        doReturn(flow {
            emit(DataState.Loading)
            emit(DataState.Success(testInputCards))
        }).`when`(useCase).invoke()
        val viewModel = HomeViewModel(useCase, bannerListUseCase)
        viewModel.uiState.test {
            assertEquals(
                CardListUiState.Success(testInputCards, emptyList()),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify(useCase).invoke()
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() =
        runTest {
            doReturn(DataState.Success(flow<List<CardData>> {
                throw RuntimeException("broken") })).`when`(
                useCase
            ).invoke()
            val viewModel = HomeViewModel(useCase, bannerListUseCase)
            viewModel.uiState.test {
                assertEquals(
                    CardListUiState.Error((RuntimeException("Error Message For You").toString())),
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
