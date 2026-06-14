package com.example.glab_s13_bpareja_2025.components.contenido

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
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
import com.example.glab_s13_bpareja_2025.components.comun.AnimatedButton

@Composable
fun ContentAnimationScreen() {
    var state by remember { mutableStateOf(ScreenState.Loading) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedButton(
            onClick = {
                state = when (state) {
                    ScreenState.Loading -> ScreenState.Content
                    ScreenState.Content -> ScreenState.Error
                    ScreenState.Error -> ScreenState.Loading
                }
            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Text("Ciclar Estado de la Aplicación")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                },
                label = "ContentState"
            ) { targetState ->
                when (targetState) {
                    ScreenState.Loading -> CircularProgressIndicator(
                        strokeWidth = 6.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ScreenState.Content -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF10B981), modifier = Modifier.size(40.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Sincronizado", style = MaterialTheme.typography.titleLarge)
                    }
                    ScreenState.Error -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Error Crítico", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
