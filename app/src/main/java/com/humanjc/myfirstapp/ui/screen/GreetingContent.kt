package com.humanjc.myfirstapp.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.humanjc.myfirstapp.ui.component.HistoryList
import com.humanjc.myfirstapp.ui.component.PressableButton
import kotlin.math.roundToInt

@Composable
fun GreetingContent(
    uiState: ButtonUiState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onMaxCountChange: (Int) -> Unit,
    onPressChanged: (Boolean) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (uiState.isPressed) 0.95f else 1f,
        animationSpec = tween(120),
        label = "scale"
    )

    val buttonColor by animateColorAsState(
        targetValue = if (uiState.isEnabled)
            Color(0xFF4CAF50)
        else
            Color.Gray,
        animationSpec = tween(300),
        label = "color"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "현재 ${uiState.count} / ${uiState.maxCount} 번 눌렀어요",
                color = if (uiState.isEnabled) Color.Black else Color.Red
            )

            Spacer(Modifier.height(16.dp))

            Text("최대 횟수 설정: ${uiState.maxCount}")
            Slider(
                value = uiState.maxCount.toFloat(),
                onValueChange = { onMaxCountChange(it.roundToInt()) },
                valueRange = 5f..20f,
                steps = 2, // 5, 10, 15, 20 중 선택 (또는 직접 값 조정)
                modifier = Modifier.padding(horizontal = 32.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF4CAF50),
                    activeTrackColor = Color(0xFF4CAF50)
                )
            )

            Spacer(Modifier.height(16.dp))

            PressableButton(
                enabled = uiState.isEnabled,
                scale = scale,
                color = buttonColor,
                onClick = onClick,
                onLongClick = onLongClick,
                onPressChanged = onPressChanged
            )

            Spacer(Modifier.height(8.dp))

            HistoryList(
                history = uiState.history,
                modifier = Modifier.height(200.dp)
            )

            Button(onClick = onReset) {
                Text("초기화")
            }
        }
    }
}