package com.app.home.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.home.presentation.HomeViewModel
import com.app.ui.TopBar

@Composable
internal fun HomeScreen(onNavigateToCard: (String) -> Unit) {
    Scaffold(topBar = {
        TopBar(title = "CardList", isHome = true) {
        }
    }) {
        val contentModifier = Modifier.padding(it)

        val viewModel = hiltViewModel<HomeViewModel>()
        val cardList =
            viewModel.uiState.collectAsStateWithLifecycle().value
        HomeUI(contentModifier, cardList, onSelected = onNavigateToCard){
            viewModel.getCardData()
        }
    }
}

