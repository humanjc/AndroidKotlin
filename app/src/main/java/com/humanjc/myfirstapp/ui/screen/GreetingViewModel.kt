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
        incrementCount(1, "버튼 클릭")
    }

    fun onLongClick() {
        incrementCount(5, "버튼 길게 클릭")
    }

    private fun incrementCount(amount: Int, actionName: String) {
        val state = _uiState.value
        if (state.isEnabled) {
            val newCount = state.count + amount
            val newHistory = listOf("$actionName: ${newCount}회 (${if (amount > 1) "+$amount" else "+1"})") + state.history

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