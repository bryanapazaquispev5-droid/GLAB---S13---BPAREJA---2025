package com.example.glab_s13_bpareja_2025.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.ScreenState

@Composable
fun ContentAnimationScreen() {
    var state by remember { mutableStateOf(ScreenState.Loading) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalButton(
            onClick = {
                state = when (state) {
                    ScreenState.Loading -> ScreenState.Content
                    ScreenState.Content -> ScreenState.Error
                    ScreenState.Error -> ScreenState.Loading
                }
            },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cycle Application State")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                },
                label = "ContentState"
            ) { targetState ->
                when (targetState) {
                    ScreenState.Loading -> CircularProgressIndicator(strokeWidth = 6.dp)
                    ScreenState.Content -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Data Synchronized", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                    ScreenState.Error -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Connection Failed", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
