package com.humanjc.myfirstapp.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GreetingViewModel : ViewModel() {
    private val _uiState = mutableStateOf(ButtonUiState())
    val uiState: State<ButtonUiState> = _uiState

    fun onButtonPress(pressed: Boolean) {
        _uiState.value = _uiState.value.copy(isPressed = pressed)
    }

    fun onClick() {
        val state = _uiState.value
        if (state.isEnabled) {
            val newCount = state.count + 1
            val newHistory = listOf("버튼 ${newCount}회 클릭") + state.history

            _uiState.value = state.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun reset() {
        _uiState.value = ButtonUiState()
    }
}