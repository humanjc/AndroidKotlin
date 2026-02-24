package com.humanjc.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.humanjc.myfirstapp.ui.screen.ButtonUiState
import com.humanjc.myfirstapp.ui.screen.GreetingContent
import com.humanjc.myfirstapp.ui.screen.GreetingScreen
import com.humanjc.myfirstapp.ui.theme.MyFirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstAppTheme {
                GreetingScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingContentPreviewWithHistory() {
    GreetingContent(
        uiState = ButtonUiState(
            count = 3,
            history = listOf(
                "버튼 1회 클릭",
                "버튼 2회 클릭",
                "버튼 3회 클릭"
            )
        ),
        onClick = {},
        onPressChanged = {},
        onReset = {}
    )
}

@Preview(
    name = "초기 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewInitial() {
    GreetingContent(
        uiState = ButtonUiState(),
        onClick = {},
        onPressChanged = {},
        onReset = {}
    )
}

@Preview(
    name = "비활성 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewDisabled() {
    GreetingContent(
        uiState = ButtonUiState(count = 10),
        onClick = {},
        onPressChanged = {},
        onReset = {}
    )
}