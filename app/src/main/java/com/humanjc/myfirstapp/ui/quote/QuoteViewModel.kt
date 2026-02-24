package com.humanjc.myfirstapp.ui.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanjc.myfirstapp.data.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuoteUiState(
    val quote: String = "명언을 불러오는 중...",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuoteUiState())
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    init {
        loadQuote()
    }

    fun loadQuote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getRandomQuote()
                .onSuccess { response ->
                    _uiState.update { it.copy(quote = response.slip.advice, isLoading = false) }
                }
                .onFailure { t ->
                    _uiState.update { it.copy(error = t.message, isLoading = false) }
                }
        }
    }
}
