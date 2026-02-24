package com.humanjc.myfirstapp.ui.navigation

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.humanjc.myfirstapp.ui.event.UiEvent
import com.humanjc.myfirstapp.ui.home.HomeContent
import com.humanjc.myfirstapp.ui.home.HomeViewModel
import com.humanjc.myfirstapp.ui.settings.SettingsContent
import com.humanjc.myfirstapp.ui.theme.MyFirstAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(viewModel: HomeViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    onClick = viewModel::onClick,
                    onLongClick = viewModel::onLongClick,
                    onPressChanged = viewModel::onButtonPress,
                    onReset = viewModel::reset,
                    onSettingsClick = { navController.navigate("settings") }
                )
            }
            composable("settings") {
                SettingsContent(
                    uiState = uiState,
                    onMaxCountChange = viewModel::onMaxCountChange,
                    onThemeChange = viewModel::onThemeChange,
                    onResetSettings = viewModel::resetSettings,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
