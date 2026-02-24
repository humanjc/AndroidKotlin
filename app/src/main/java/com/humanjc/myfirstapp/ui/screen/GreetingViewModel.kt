package com.humanjc.myfirstapp.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    object Vibrate : UiEvent()
}

class GreetingViewModel : ViewModel() {
    private val _uiState = mutableStateOf(ButtonUiState())
    val uiState: State<ButtonUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onButtonPress(pressed: Boolean) {
        _uiState.value = _uiState.value.copy(isPressed = pressed)
    }

    fun onMaxCountChange(newMax: Int) {
        _uiState.value = _uiState.value.copy(maxCount = newMax)
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

            if (amount > 1) {
                emitEvent(UiEvent.Vibrate)
            }

            if (newCount >= state.maxCount) {
                emitEvent(UiEvent.ShowToast("최대 횟수에 도달했습니다!"))
            }
        }
    }

    fun reset() {
        _uiState.value = ButtonUiState()
        emitEvent(UiEvent.ShowSnackbar("모든 기록이 초기화되었습니다."))
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}