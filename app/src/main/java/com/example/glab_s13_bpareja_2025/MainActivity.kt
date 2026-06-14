package com.example.glab_s13_bpareja_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.ui.theme.GLABS13BPAREJA2025Theme

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.togetherWith
import androidx.compose.material3.CircularProgressIndicator

enum class ScreenState { Loading, Content, Error }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GLABS13BPAREJA2025Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VisibilityAnimationScreen()
                        Divider()
                        ColorAnimationScreen()
                        Divider()
                        SizeAndPositionAnimationScreen()
                        Divider()
                        ContentAnimationScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ContentAnimationScreen() {
    var state by remember { mutableStateOf(ScreenState.Loading) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Exercise 4: Content Animation")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            state = when (state) {
                ScreenState.Loading -> ScreenState.Content
                ScreenState.Content -> ScreenState.Error
                ScreenState.Error -> ScreenState.Loading
            }
        }) {
            Text(text = "Next State")
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            },
            label = "ContentTransition"
        ) { targetState ->
            when (targetState) {
                ScreenState.Loading -> {
                    CircularProgressIndicator()
                }
                ScreenState.Content -> {
                    Text("Success! Content Loaded.", color = Color.Green)
                }
                ScreenState.Error -> {
                    Text("Error! Something went wrong.", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun SizeAndPositionAnimationScreen() {
    var isMoved by remember { mutableStateOf(false) }
    val size by animateDpAsState(targetValue = if (isMoved) 150.dp else 100.dp)
    val offsetX by animateDpAsState(targetValue = if (isMoved) 50.dp else 0.dp)
    val offsetY by animateDpAsState(targetValue = if (isMoved) 50.dp else 0.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Exercise 3: Size and Position Animation")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { isMoved = !isMoved }) {
            Text(text = "Move and Resize")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .offset(x = offsetX, y = offsetY)
                .size(size)
                .background(Color.Red)
        )
        // Add extra space to avoid overlapping when moved
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier.height(32.dp))
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun VisibilityAnimationScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Exercise 1: Visibility Animation")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = if (isVisible) "Hide Box" else "Show Box")
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Composable
fun ColorAnimationScreen() {
    var isBlue by remember { mutableStateOf(true) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isBlue) Color.Blue else Color.Green,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Exercise 2: Color Animation")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { isBlue = !isBlue }) {
            Text(text = "Change Color")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(backgroundColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GLABS13BPAREJA2025Theme {
        Column {
            VisibilityAnimationScreen()
            ColorAnimationScreen()
        }
    }
}