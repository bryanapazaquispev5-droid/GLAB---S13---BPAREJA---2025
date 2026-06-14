package com.example.glab_s13_bpareja_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        VisibilityAnimationScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun VisibilityAnimationScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
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

@Preview(showBackground = true)
@Composable
fun VisibilityAnimationPreview() {
    GLABS13BPAREJA2025Theme {
        VisibilityAnimationScreen()
    }
}