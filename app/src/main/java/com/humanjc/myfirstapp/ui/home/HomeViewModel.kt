package com.humanjc.myfirstapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanjc.myfirstapp.data.repository.CounterRepository
import com.humanjc.myfirstapp.ui.event.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CounterRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        // 설정 데이터 불러오기
        viewModelScope.launch {
            val count = repository.countFlow.first()
            val maxCount = repository.maxCountFlow.first()
            val isDarkMode = repository.isDarkModeFlow.first() ?: false
            
            _uiState.update { 
                it.copy(
                    count = count,
                    maxCount = maxCount,
                    isDarkMode = isDarkMode
                )
            }
        }

        // 히스토리 실시간 구독 (Room Flow)
        viewModelScope.launch {
            repository.historyFlow.collect { records ->
                _uiState.update { state ->
                    state.copy(history = records.map { it.toDisplayString() })
                }
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

                if (amount > 1) {
                    emitEvent(UiEvent.Vibrate)
                }

                if (newCount >= state.maxCount) {
                    emitEvent(UiEvent.ShowToast("최대 횟수에 도달했습니다!"))
                }
                
                val newState = state.copy(count = newCount)
                
                // Room에 기록 저장
                viewModelScope.launch {
                    repository.addClickRecord(newCount, actionName)
                }
                
                saveToDataStore(newState)
                newState
            } else {
                state
            }
        }
    }

    fun reset() {
        _uiState.update { it.copy(count = 0) }
        emitEvent(UiEvent.ShowSnackbar("활동 기록이 초기화되었습니다."))
        viewModelScope.launch {
            repository.clearHistory()
            saveToDataStore()
        }
    }

    fun resetSettings() {
        _uiState.update { it.copy(maxCount = 10, isDarkMode = false) }
        emitEvent(UiEvent.ShowSnackbar("설정값이 초기화되었습니다."))
        saveToDataStore()
    }

    private fun saveToDataStore(state: HomeUiState = _uiState.value) {
        viewModelScope.launch {
            repository.saveSettings(state.count, state.maxCount, state.isDarkMode)
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
