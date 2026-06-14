package com.example.glab_s13_bpareja_2025.components.videojuego

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HealthBar(
    health: Float,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    // Pulsing effect for the neon glow shadow
    val infiniteTransition = rememberInfiniteTransition(label = "hudPulse")
    val pulseGlowVal by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hudPulseGlow"
    )

    val animatedHealth by animateFloatAsState(
        targetValue = health,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "healthAnim"
    )

    Column(
        modifier = modifier.width(220.dp)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        
        // Container for health bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .shadow(
                    elevation = pulseGlowVal.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false,
                    ambientColor = color,
                    spotColor = color
                )
                .background(Color(0xFF090D1A), RoundedCornerShape(8.dp))
                .border(1.5.dp, color.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(2.dp)
        ) {
            // LED Glow Core with hot-core white gradient
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animatedHealth.coerceIn(0f, 1f))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,       // hot-core white
                                color,             // base neon color
                                color.copy(alpha = 0.7f)
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}
