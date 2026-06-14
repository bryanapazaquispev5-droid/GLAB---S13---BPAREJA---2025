package com.example.glab_s13_bpareja_2025.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.components.comun.AnimatedButton

@Composable
fun ColorAnimationScreen() {
    var isAlternative by remember { mutableStateOf(false) }
    val color1 = MaterialTheme.colorScheme.primary
    val color2 = Color(0xFF00E676) // Neon Green
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isAlternative) color2 else color1,
        animationSpec = tween(durationMillis = 1000),
        label = "ColorTransition"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedButton(
            onClick = { isAlternative = !isAlternative },
            containerColor = backgroundColor,
            contentColor = if (isAlternative) Color.Black else Color.White
        ) {
            Text("Cambiar Color de Tema")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(6.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .padding(12.dp)
                .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
        )
    }
}
