package com.example.glab_s13_bpareja_2025.components.comun

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun Joystick(
    modifier: Modifier = Modifier,
    size: Float = 150f,
    onMove: (x: Float, y: Float) -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val radiusPx = remember(size) { with(density) { (size / 2).dp.toPx() } }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size((size / 2.5).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF06B6D4), Color(0xFF6366F1))
                    )
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            offsetX = 0f
                            offsetY = 0f
                            onMove(0f, 0f)
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            
                            val newX = offsetX + dragAmount.x
                            val newY = offsetY + dragAmount.y
                            val distance = sqrt(newX.pow(2) + newY.pow(2))
                            
                            if (distance <= radiusPx) {
                                offsetX = newX
                                offsetY = newY
                            } else {
                                val angle = atan2(newY, newX)
                                offsetX = cos(angle) * radiusPx
                                offsetY = sin(angle) * radiusPx
                            }
                            
                            // Normalize output -1.0 to 1.0
                            onMove(offsetX / radiusPx, offsetY / radiusPx)
                        }
                    )
                }
        )
    }
}
