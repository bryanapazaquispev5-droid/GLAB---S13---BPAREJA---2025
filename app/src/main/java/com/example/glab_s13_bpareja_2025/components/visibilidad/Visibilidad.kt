package com.example.glab_s13_bpareja_2025.components.visibilidad

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.dp

@Composable
fun VisibilityAnimationScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { isVisible = !isVisible },
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
            Spacer(Modifier.width(8.dp))
            Text(if (isVisible) "Ocultar Elemento" else "Mostrar Elemento")
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(600)) + scaleIn(tween(600)),
            exit = fadeOut(tween(600)) + scaleOut(tween(600))
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                        )
                    )
                    .shadow(8.dp, RoundedCornerShape(24.dp))
            )
        }
    }
}
