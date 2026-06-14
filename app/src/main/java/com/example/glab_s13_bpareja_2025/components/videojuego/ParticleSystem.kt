package com.example.glab_s13_bpareja_2025.components.videojuego

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

object ParticleSystem {
    // Generar una explosión de chispas retro neón
    fun createExplosion(startId: Long, position: Offset, color: Color, count: Int = 12): List<Particle> {
        val particles = mutableListOf<Particle>()
        for (i in 0 until count) {
            val angle = Math.random() * 2.0 * PI
            val speed = (3.0f + Math.random() * 6.0f).toFloat() // Velocidades aleatorias
            val velocity = Offset(
                (cos(angle) * speed).toFloat(),
                (sin(angle) * speed).toFloat()
            )
            val maxAge = (18..32).random() // Duración aleatoria en frames (~300ms a ~500ms)
            particles.add(
                Particle(
                    id = startId + i,
                    position = position,
                    velocity = velocity,
                    color = color,
                    maxAge = maxAge,
                    age = 0
                )
            )
        }
        return particles
    }
}
