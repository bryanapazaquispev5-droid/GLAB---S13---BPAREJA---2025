package com.example.glab_s13_bpareja_2025.components.videojuego

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

// Tipos de Power-Up del juego
enum class PowerUpType {
    SHIELD,       // Otorga un escudo de energía cian protector
    DOUBLE_SHOT   // Habilita disparo doble por 8 segundos
}

// Proyectiles en pantalla
data class Projectile(
    val id: Long,
    val position: Offset,
    val velocity: Offset,
    val isPlayer: Boolean,
    val isSineWave: Boolean = false,
    val startY: Float = 0f,
    val age: Int = 0
)

// Partículas de texto flotante (Daño, combos, esquivas)
data class DamageText(
    val id: Long,
    val text: String,
    val position: Offset,
    val alpha: Float,
    val color: Color
)

// Estrellas fugaces en el Canvas
data class ShootingStar(
    val id: Long,
    val position: Offset,
    val velocity: Offset,
    val length: Float,
    val alpha: Float
)

// Estrellas de fondo desplazables (Paralaje)
data class BackgroundStar(
    val id: Long,
    val position: Offset,
    val size: Float,
    val speed: Float,
    val alpha: Float
)

// Nebulosas de color gaseosas de fondo
data class NebulaCloud(
    val id: Long,
    val position: Offset,
    val size: Float,
    val color: Color,
    val speed: Float
)

// Cinturón de asteroides de fondo
data class SpaceAsteroid(
    val id: Long,
    val position: Offset,
    val size: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val emoji: String
)

// Ítems de Power-Up flotantes
data class PowerUp(
    val id: Long,
    val position: Offset,
    val type: PowerUpType,
    val size: Float,
    val speed: Float
)

// Partícula de chispas para explosiones
data class Particle(
    val id: Long,
    val position: Offset,
    val velocity: Offset,
    val color: Color,
    val maxAge: Int,
    val age: Int
)
