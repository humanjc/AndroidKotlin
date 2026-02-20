package com.humanjc.myfirstapp.ui.screen

data class ButtonUiState(
    val count: Int = 0,
    val maxCount: Int = 10,
    val isPressed: Boolean = false
) {
    val isEnabled: Boolean
        get() = count < maxCount
}