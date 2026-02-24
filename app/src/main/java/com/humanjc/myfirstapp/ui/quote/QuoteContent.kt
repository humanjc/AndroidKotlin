package com.humanjc.myfirstapp.ui.quote

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteContent(
    uiState: QuoteUiState,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 타이핑 효과를 위한 상태
    var displayedText by remember { mutableStateOf("") }
    
    // 명언이 변경될 때마다 타이핑 애니메이션 실행
    LaunchedEffect(uiState.quote) {
        displayedText = ""
        uiState.quote.forEach { char ->
            displayedText += char
            delay(60) // 타이핑 속도 조절
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("오늘의 명언", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onRefreshClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "새로고침")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("지혜를 가져오는 중...")
                }
            } else if (uiState.error != null) {
                Text(text = "오류 발생: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else {
                // 전체 컨텐츠가 부드럽게 위로 솟아오르며 나타나는 효과
                AnimatedVisibility(
                    visible = !uiState.isLoading,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(animationSpec = tween(800)),
                    exit = fadeOut()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            ),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Box(modifier = Modifier.padding(32.dp)) {
                                Text(
                                    text = "\"$displayedText\"", // 타이핑되는 텍스트 사용
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        lineHeight = 36.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Text(
                            text = "- Advice Slip API -",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
