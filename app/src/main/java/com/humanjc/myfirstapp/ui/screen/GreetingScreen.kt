package com.humanjc.myfirstapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GreetingScreen(
    modifier: Modifier = Modifier,
    viewModel: GreetingViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    GreetingContent(
        uiState = uiState,
        onClick = viewModel::onClick,
        onLongClick = viewModel::onLongClick,
        onPressChanged = viewModel::onButtonPress,
        onReset = viewModel::reset,
        modifier = modifier
    )
}