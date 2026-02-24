package com.humanjc.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.humanjc.myfirstapp.ui.home.HomeContent
import com.humanjc.myfirstapp.ui.home.HomeUiState
import com.humanjc.myfirstapp.ui.navigation.AppNavigation
import com.humanjc.myfirstapp.ui.theme.MyFirstAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingContentPreviewWithHistory() {
    val snackbarHostState = remember { SnackbarHostState() }
    MyFirstAppTheme {
        HomeContent(
            uiState = HomeUiState(
                count = 3,
                history = listOf(
                    "버튼 1회 클릭",
                    "버튼 2회 클릭",
                    "버튼 3회 클릭"
                )
            ),
            snackbarHostState = snackbarHostState,
            onClick = {},
            onLongClick = {},
            onPressChanged = {},
            onReset = {},
            onSettingsClick = {},
            onQuoteClick = {}
        )
    }
}
