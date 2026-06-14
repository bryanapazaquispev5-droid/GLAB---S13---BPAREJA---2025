package com.example.glab_s13_bpareja_2025.components.videojuego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun GameBackground(
    nebulae: List<NebulaCloud>,
    scrollingStars: List<BackgroundStar>,
    asteroids: List<SpaceAsteroid>,
    shootingStars: List<ShootingStar>,
    screenWidthDp: Dp,
    screenHeightDp: Dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 1. Nebulosas gigantes de fondo con degradados radiales suaves
        nebulae.forEach { n ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(n.position.x.roundToInt(), n.position.y.roundToInt()) }
                    .size(n.size.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(n.color.copy(alpha = 0.07f), Color.Transparent)
                        )
                    )
            )
        }

        // 2. Estrellas con desplazamiento por Parallax 3D
        scrollingStars.forEach { s ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(s.position.x.roundToInt(), s.position.y.roundToInt()) }
                    .size(s.size.dp)
                    .alpha(s.alpha)
                    .background(Color.White, CircleShape)
            )
        }

        // 3. Cinturón de Asteroides y Cometas en deriva
        asteroids.forEach { a ->
            Text(
                text = a.emoji,
                fontSize = a.size.sp,
                modifier = Modifier
                    .offset { IntOffset(a.position.x.roundToInt(), a.position.y.roundToInt()) }
                    .graphicsLayer {
                        rotationZ = a.rotation
                        alpha = 0.18f
                    }
            )
        }

        // 4. Planetas Holográficos Retro-Cyber
        Box(
            modifier = Modifier
                .offset(x = screenWidthDp * 0.78f, y = 110.dp)
                .size(160.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFD946EF).copy(alpha = 0.12f), Color(0xFF6366F1).copy(alpha = 0.03f), Color.Transparent)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.3f, 0.35f)
                    .border(2.dp, Color(0xFFD946EF).copy(alpha = 0.08f), CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .offset(x = 120.dp, y = screenHeightDp * 0.65f)
                .size(100.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF06B6D4).copy(alpha = 0.12f), Color(0xFF0F172A).copy(alpha = 0.02f), Color.Transparent)
                    )
                )
        )

        // 5. Renderizado de Estrellas Fugaces en Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            shootingStars.forEach { s ->
                val vMag = sqrt(s.velocity.x * s.velocity.x + s.velocity.y * s.velocity.y)
                val unitOpposite = Offset(-s.velocity.x / vMag, -s.velocity.y / vMag)
                val tailEnd = s.position + unitOpposite * s.length
                
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = s.alpha), Color.Transparent),
                        start = s.position,
                        end = tailEnd
                    ),
                    start = s.position,
                    end = tailEnd,
                    strokeWidth = 3.5f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
