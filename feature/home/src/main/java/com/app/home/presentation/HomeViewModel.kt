package com.app.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.core.utils.DataState
import com.app.domain.model.Banner
import com.app.domain.model.CardData
import com.app.domain.usecase.GetBannerListUseCase
import com.app.domain.usecase.GetCardListUseCase
import com.app.home.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val cardListUseCase: GetCardListUseCase,
    private val bannerListUseCase: GetBannerListUseCase
) :
    ViewModel() {
    private val _mutableCardsState = MutableStateFlow<CardListUiState>(CardListUiState.Init)

    private val _cardDataFlow = MutableStateFlow<List<CardData>>(emptyList())
    private val _bannerDataFlow = MutableStateFlow<List<Banner>>(emptyList())

    // Flow to manage the overall UI state
    private val _uiState = MutableStateFlow<CardListUiState>(CardListUiState.Init)

    val uiState: StateFlow<CardListUiState> = _uiState
    //val uiState: StateFlow<CardListUiState> = _mutableCardsState

    // Publicly exposed StateFlow that combines and manages the UI state
    init {
        /*combine(_cardDataFlow, _bannerDataFlow) { cardData, bannerData ->
            if (cardData.isNotEmpty() || bannerData.isNotEmpty()) {
                CardListUiState.Success(cardData = cardData, bannerData = bannerData)
            } else {
                CardListUiState.Loading
            }
        }.onEach { combinedState ->
            _uiState.value = combinedState
        }.launchIn(viewModelScope)*/
        getCardData()
        //getBannerData()
    }

    /*    private fun interleaveData(listA: List<CardData>, listB: List<Banner>): List<Any> {
            val resultList = mutableListOf<Any>()
            var indexB = 0

            listA.forEachIndexed { index, itemA ->
                resultList.add(itemA)
                // After every two items from listA, add one item from listB (if available)
                if ((index + 1) % 2 == 0 && indexB < listB.size) {
                    resultList.add(listB[indexB])
                    indexB++
                }
            }

            // If there are remaining items in listB, add them to the result list
            if (indexB < listB.size) {
                resultList.addAll(listB.subList(indexB, listB.size))
            }

            return resultList
        }*/

    internal fun getCardData() {
        //_mutableCardsState.value = CardListUiState.Loading
        viewModelScope.launch {
            //_mutableCardsState.value = CardListUiState.Loading
            cardListUseCase().collect {
                when (it) {
                    is DataState.Success -> {
                        _cardDataFlow.value = it.result
                        _uiState.value = CardListUiState.Success(it.result, emptyList())
                    }

                    is DataState.Error -> {
                        //_mutableCardsState.value = CardListUiState.Error(it.exception.errorMessage)
                        _uiState.value = CardListUiState.Error(it.exception.errorMessage)
                    }

                    is DataState.Loading -> {
                        //_mutableCardsState.value = CardListUiState.Loading
                        _uiState.value = CardListUiState.Loading
                    }
                }

            }
        }
    }

    private fun getBannerData() {
        //_mutableCardsState.value = CardListUiState.Loading
        viewModelScope.launch {
            //_mutableCardsState.value = CardListUiState.Loading
            bannerListUseCase.invoke().collect {
                when (it) {
                    is DataState.Success -> {
                        _bannerDataFlow.value = it.result
                    }

                    is DataState.Error -> {
                        _uiState.value = CardListUiState.Error(it.exception.message)
                    }

                    is DataState.Loading -> {
                        //  _uiState.value = CardListUiState.Loading
                    }
                }
            }
        }
    }


}
