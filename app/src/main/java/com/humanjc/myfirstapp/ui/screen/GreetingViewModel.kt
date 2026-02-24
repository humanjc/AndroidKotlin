package com.humanjc.myfirstapp.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.humanjc.myfirstapp.data.CounterDataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    object Vibrate : UiEvent()
}

class GreetingViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = CounterDataStore(application)
    
    private val _uiState = MutableStateFlow(ButtonUiState())
    val uiState: StateFlow<ButtonUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        // 앱 시작 시 저장된 데이터 불러오기
        viewModelScope.launch {
            val count = dataStore.countFlow.first()
            val maxCount = dataStore.maxCountFlow.first()
            val history = dataStore.historyFlow.first()
            val isDarkMode = dataStore.isDarkModeFlow.first() ?: false
            
            _uiState.update { 
                it.copy(
                    count = count,
                    maxCount = maxCount,
                    history = history,
                    isDarkMode = isDarkMode
                )
            }
        }
    }

    fun onThemeChange(isDark: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDark) }
        saveToDataStore()
    }

    fun onButtonPress(pressed: Boolean) {
        _uiState.update { it.copy(isPressed = pressed) }
    }

    fun onMaxCountChange(newMax: Int) {
        _uiState.update { it.copy(maxCount = newMax) }
        saveToDataStore()
    }

    fun onClick() {
        incrementCount(1, "버튼 클릭")
    }

    fun onLongClick() {
        incrementCount(5, "버튼 길게 클릭")
    }

    private fun incrementCount(amount: Int, actionName: String) {
        _uiState.update { state ->
            if (state.isEnabled) {
                val newCount = (state.count + amount).coerceAtMost(state.maxCount)
                val newHistory = listOf("$actionName: ${newCount}회 (${if (amount > 1) "+$amount" else "+1"})") + state.history

                if (amount > 1) {
                    emitEvent(UiEvent.Vibrate)
                }

                if (newCount >= state.maxCount) {
                    emitEvent(UiEvent.ShowToast("최대 횟수에 도달했습니다!"))
                }
                
                val newState = state.copy(
                    count = newCount,
                    history = newHistory
                )
                
                saveToDataStore(newState)
                newState
            } else {
                state
            }
        }
    }

    fun reset() {
        _uiState.update { ButtonUiState() }
        emitEvent(UiEvent.ShowSnackbar("모든 기록이 초기화되었습니다."))
        viewModelScope.launch {
            dataStore.clearData()
        }
    }

    private fun saveToDataStore(state: ButtonUiState = _uiState.value) {
        viewModelScope.launch {
            dataStore.saveCounterData(state.count, state.maxCount, state.history, state.isDarkMode)
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
