package com.humanjc.myfirstapp.ui.screen

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.viewmodel.compose.viewModel
import com.humanjc.myfirstapp.ui.theme.MyFirstAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GreetingScreen(
    modifier: Modifier = Modifier,
    viewModel: GreetingViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.Vibrate -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }
    }

    MyFirstAppTheme(darkTheme = uiState.isDarkMode) {
        GreetingContent(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onClick = viewModel::onClick,
            onLongClick = viewModel::onLongClick,
            onMaxCountChange = viewModel::onMaxCountChange,
            onThemeChange = viewModel::onThemeChange,
            onPressChanged = viewModel::onButtonPress,
            onReset = viewModel::reset,
            modifier = modifier
        )
    }
}