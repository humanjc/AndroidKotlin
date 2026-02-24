package com.humanjc.myfirstapp.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.humanjc.myfirstapp.R
import com.humanjc.myfirstapp.ui.component.HistoryList
import com.humanjc.myfirstapp.ui.component.PressableButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onPressChanged: (Boolean) -> Unit,
    onReset: () -> Unit,
    onSettingsClick: () -> Unit,
    onQuoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (uiState.isPressed) 0.92f else 1f,
        animationSpec = tween(120),
        label = "scale"
    )

    val buttonColor by animateColorAsState(
        targetValue = if (uiState.isEnabled)
            Color(0xFF4CAF50)
        else
            Color.LightGray,
        animationSpec = tween(300),
        label = "color"
    )

    // 1. 로컬 리소스에서 Lottie 파일 불러오기 (R.raw.confetti)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.confetti)
    )
    
    val isMaxReached = uiState.count >= uiState.maxCount

    // 2. 애니메이션 상태 관리
    val lottieProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isMaxReached,
        restartOnPlay = true,
        iterations = 1
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("카운터 앱", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onQuoteClick) {
                        Icon(Icons.Default.Info, contentDescription = "명언")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "설정")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 카운터 요약 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "누적 횟수",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            AnimatedContent(
                                targetState = uiState.count,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                            slideOutVertically { height -> -height } + fadeOut())
                                    } else {
                                        (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                            slideOutVertically { height -> height } + fadeOut())
                                    }.using(SizeTransform(clip = false))
                                },
                                label = "countAnimation"
                            ) { count ->
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                                    fontWeight = FontWeight.Black,
                                    color = if (uiState.isEnabled) MaterialTheme.colorScheme.onPrimaryContainer else Color.Red
                                )
                            }
                            
                            Text(
                                text = " / ${uiState.maxCount}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                PressableButton(
                    enabled = uiState.isEnabled,
                    scale = scale,
                    color = buttonColor,
                    onClick = onClick,
                    onLongClick = onLongClick,
                    onPressChanged = onPressChanged
                )

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "활동 기록",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = onReset,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("초기화")
                    }
                }

                Spacer(Modifier.height(12.dp))

                HistoryList(
                    history = uiState.history,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            // 3. 폭죽 애니메이션을 화면 최상단 레이어에 배치
            if (isMaxReached && lottieProgress < 1f) {
                LottieAnimation(
                    composition = composition,
                    progress = { lottieProgress },
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
        }
    }
}
