package com.example.glab_s13_bpareja_2025.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorAnimationScreen() {
    var isAlternative by remember { mutableStateOf(false) }
    val color1 = MaterialTheme.colorScheme.primary
    val color2 = Color(0xFF4CAF50) // Verde Moderno
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isAlternative) color2 else color1,
        animationSpec = tween(durationMillis = 800),
        label = "ColorTransition"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { isAlternative = !isAlternative },
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cambiar Color de Tema")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape)
        )
    }
}
