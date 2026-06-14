package com.example.glab_s13_bpareja_2025.components.visibilidad

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.components.comun.AnimatedButton

@Composable
fun VisibilityAnimationScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedButton(
            onClick = { isVisible = !isVisible }
        ) {
            Icon(if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
            Spacer(Modifier.width(8.dp))
            Text(if (isVisible) "Ocultar Elemento" else "Mostrar Elemento")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(800)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = fadeOut(tween(500)) + shrinkOut(tween(500))
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                                Color(0xFF6200EE)
                            )
                        )
                    )
                    .shadow(12.dp, RoundedCornerShape(32.dp))
            )
        }
    }
}
