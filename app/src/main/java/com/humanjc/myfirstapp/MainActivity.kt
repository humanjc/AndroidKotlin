package com.humanjc.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.humanjc.myfirstapp.ui.home.HomeContent
import com.humanjc.myfirstapp.ui.home.HomeUiState
import com.humanjc.myfirstapp.ui.home.HomeViewModel
import com.humanjc.myfirstapp.ui.navigation.AppNavigation
import com.humanjc.myfirstapp.ui.theme.MyFirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HomeViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            MyFirstAppTheme(darkTheme = uiState.isDarkMode) {
                AppNavigation(viewModel = viewModel)
            }
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
            onSettingsClick = {}
        )
    }
}
