package com.app.home.presentation

import com.app.domain.model.Banner
import com.app.domain.model.CardData


sealed interface CardListUiState {

    object Init : CardListUiState

    data class Success(
        val cardData: List<CardData>,
        val bannerData : List<Banner>
    ) : CardListUiState

    object Loading : CardListUiState
    data class Error(val message: String) : CardListUiState
}
