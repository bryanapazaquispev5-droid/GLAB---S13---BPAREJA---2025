package com.example.glab_s13_bpareja_2025.components.videojuego

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glab_s13_bpareja_2025.components.comun.Joystick
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class Projectile(
    val id: Long,
    val position: Offset,
    val velocity: Offset,
    val isPlayer: Boolean,
    val isSineWave: Boolean = false,
    val startY: Float = 0f,
    val age: Int = 0
)

data class DamageText(
    val id: Long,
    val text: String,
    val position: Offset,
    val alpha: Float,
    val color: Color
)

data class ShootingStar(
    val id: Long,
    val position: Offset,
    val velocity: Offset,
    val length: Float,
    val alpha: Float
)

data class BackgroundStar(
    val id: Long,
    val position: Offset,
    val size: Float,
    val speed: Float,
    val alpha: Float
)

data class NebulaCloud(
    val id: Long,
    val position: Offset,
    val size: Float,
    val color: Color,
    val speed: Float
)

data class SpaceAsteroid(
    val id: Long,
    val position: Offset,
    val size: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val emoji: String
)

@Composable
fun GameScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    
    // Bloquear a Horizontal e iniciar música de fondo (BGM)
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        BgmManager.start()
        onDispose {
            BgmManager.stop()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        val density = LocalDensity.current
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }

        val screenWidthDp = maxWidth
        val screenHeightDp = maxHeight

        val playerSizeDp = 70.dp
        val enemySizeDp = 120.dp
        val playerSizePx = with(density) { playerSizeDp.toPx() }
        val enemySizePx = with(density) { enemySizeDp.toPx() }

        val hudHeightDp = 100.dp
        val hudHeightPx = with(density) { hudHeightDp.toPx() }

        var playerPos by remember { mutableStateOf(Offset(200f, 400f)) }
        var enemyPos by remember { mutableStateOf(Offset(1200f, 400f)) }

        // Elementos dinámicos del fondo espacial (paralaje y atmósfera)
        var scrollingStars by remember { mutableStateOf(emptyList<BackgroundStar>()) }
        var nebulae by remember { mutableStateOf(emptyList<NebulaCloud>()) }
        var asteroids by remember { mutableStateOf(emptyList<SpaceAsteroid>()) }
        
        // Posicionar relativamente cuando las dimensiones del mapa estén listas
        LaunchedEffect(screenWidthPx, screenHeightPx) {
            if (screenWidthPx > 0f && screenHeightPx > 0f) {
                playerPos = Offset(screenWidthPx * 0.15f, screenHeightPx * 0.5f)
                enemyPos = Offset(screenWidthPx * 0.8f, screenHeightPx * 0.5f)

                // Inicializar estrellas desplazables en base a 3 planos de profundidad
                scrollingStars = List(45) { i ->
                    val size = if (i < 20) {
                        (1.0f + Math.random() * 0.5f).toFloat() // Plano lejano
                    } else if (i < 35) {
                        (1.8f + Math.random() * 0.7f).toFloat() // Plano medio
                    } else {
                        (2.8f + Math.random() * 1.0f).toFloat() // Plano cercano
                    }

                    val speed = if (i < 20) {
                        (0.6f + Math.random() * 0.6f).toFloat()
                    } else if (i < 35) {
                        (1.6f + Math.random() * 1.0f).toFloat()
                    } else {
                        (3.2f + Math.random() * 1.8f).toFloat()
                    }

                    val alpha = if (i < 20) {
                        (0.2f + Math.random() * 0.15f).toFloat()
                    } else if (i < 35) {
                        (0.4f + Math.random() * 0.15f).toFloat()
                    } else {
                        (0.6f + Math.random() * 0.2f).toFloat()
                    }

                    BackgroundStar(
                        id = i.toLong(),
                        position = Offset(
                            (Math.random() * screenWidthPx).toFloat(),
                            (Math.random() * screenHeightPx).toFloat()
                        ),
                        size = size,
                        speed = speed,
                        alpha = alpha
                    )
                }

                // Inicializar 3 nubes nebulares gigantes y lentas en el fondo
                nebulae = listOf(
                    NebulaCloud(0L, Offset(screenWidthPx * 0.15f, screenHeightPx * 0.1f), 300f, Color(0xFFD946EF), 0.12f),
                    NebulaCloud(1L, Offset(screenWidthPx * 0.55f, screenHeightPx * 0.4f), 360f, Color(0xFF6366F1), 0.08f),
                    NebulaCloud(2L, Offset(screenWidthPx * 0.82f, screenHeightPx * 0.65f), 280f, Color(0xFF06B6D4), 0.15f)
                )

                // Inicializar cinturón de asteroides y cometas en el fondo
                asteroids = List(6) { i ->
                    val size = (20 + Math.random() * 20).toFloat()
                    val speed = (0.7f + Math.random() * 0.8f).toFloat()
                    val rotSpeed = (-2.0f + Math.random() * 4.0f).toFloat()
                    SpaceAsteroid(
                        id = i.toLong(),
                        position = Offset(
                            (Math.random() * screenWidthPx).toFloat(),
                            (Math.random() * screenHeightPx).toFloat()
                        ),
                        size = size,
                        speed = speed,
                        rotation = (Math.random() * 360f).toFloat(),
                        rotationSpeed = rotSpeed,
                        emoji = if (i % 2 == 0) "🪨" else "☄️"
                    )
                }
            }
        }

        var playerHealth by remember { mutableStateOf(1f) }
        var enemyHealth by remember { mutableStateOf(1f) }
        var projectiles by remember { mutableStateOf(emptyList<Projectile>()) }
        var isGameOver by remember { mutableStateOf(false) }
        var isVictory by remember { mutableStateOf(false) }
        var nextId by remember { mutableStateOf(0L) }

        var joystickVector by remember { mutableStateOf(Offset.Zero) }
        var enemyShootCooldown by remember { mutableStateOf(0) }
        var playerShootCooldown by remember { mutableStateOf(0) }

        // Estados para la invulnerabilidad (esquivas) y textos flotantes
        var isInvincible by remember { mutableStateOf(false) }
        var invincibleFramesLeft by remember { mutableStateOf(0) }
        var damageTexts by remember { mutableStateOf(emptyList<DamageText>()) }
        
        // Estado para las estrellas fugaces
        var shootingStars by remember { mutableStateOf(emptyList<ShootingStar>()) }

        // Estados y animaciones de resorte "pop" al recibir impactos de balas
        var playerScaleTarget by remember { mutableStateOf(1f) }
        var enemyScaleTarget by remember { mutableStateOf(1f) }
        var playerHitFramesLeft by remember { mutableStateOf(0) }
        var enemyHitFramesLeft by remember { mutableStateOf(0) }

        val playerScale by animateFloatAsState(
            targetValue = playerScaleTarget,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "playerScale"
        )
        val enemyScale by animateFloatAsState(
            targetValue = enemyScaleTarget,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "enemyScale"
        )

        // Animación de pulso infinito (Alpha) durante invulnerabilidad
        val infiniteTransition = rememberInfiniteTransition(label = "playerPulse")
        val invincibleAlphaPulse by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(120, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "invincibleAlpha"
        )

        // Bucle del Juego (~60 FPS)
        LaunchedEffect(isGameOver, screenWidthPx, screenHeightPx) {
            if (screenWidthPx <= 0f || screenHeightPx <= 0f) return@LaunchedEffect
            while (!isGameOver) {
                delay(16)

                // 1. Mover jugador por joystick de manera continua y aplicar límites de mapa
                if (joystickVector != Offset.Zero) {
                    val speed = 14f
                    val nextX = (playerPos.x + joystickVector.x * speed).coerceIn(0f, screenWidthPx - playerSizePx)
                    val nextY = (playerPos.y + joystickVector.y * speed).coerceIn(hudHeightPx, screenHeightPx - playerSizePx)
                    playerPos = Offset(nextX, nextY)
                }

                // 2. Gestionar invulnerabilidad
                if (invincibleFramesLeft > 0) {
                    invincibleFramesLeft--
                    if (invincibleFramesLeft == 0) {
                        isInvincible = false
                    }
                }

                // 3. Gestionar frames de escala "pop" para el jugador y enemigo
                if (playerHitFramesLeft > 0) {
                    playerHitFramesLeft--
                    if (playerHitFramesLeft == 0) {
                        playerScaleTarget = 1f
                    }
                }
                if (enemyHitFramesLeft > 0) {
                    enemyHitFramesLeft--
                    if (enemyHitFramesLeft == 0) {
                        enemyScaleTarget = 1f
                    }
                }

                // 4. Mover y atenuar textos de daño flotantes
                damageTexts = damageTexts.map {
                    it.copy(
                        position = it.position.copy(y = it.position.y - 2.5f),
                        alpha = (it.alpha - 0.03f).coerceAtLeast(0f)
                    )
                }.filter { it.alpha > 0f }

                // 5. Desplazar estrellas del fondo (Paralaje 3D)
                scrollingStars = scrollingStars.map { s ->
                    var nextX = s.position.x - s.speed
                    if (nextX < -30f) {
                        nextX = screenWidthPx + 30f
                    }
                    s.copy(position = Offset(nextX, s.position.y))
                }

                // 6. Desplazar nebulas gigantes
                nebulae = nebulae.map { n ->
                    var nextX = n.position.x - n.speed
                    val sizePx = with(density) { n.size.dp.toPx() }
                    if (nextX < -sizePx) {
                        nextX = screenWidthPx + 50f
                    }
                    n.copy(position = Offset(nextX, n.position.y))
                }

                // 7. Desplazar y rotar asteroides / cometas
                asteroids = asteroids.map { a ->
                    var nextX = a.position.x - a.speed
                    if (nextX < -150f) {
                        nextX = screenWidthPx + 150f
                    }
                    a.copy(
                        position = Offset(nextX, a.position.y),
                        rotation = (a.rotation + a.rotationSpeed) % 360f
                    )
                }

                // 8. Actualizar estrellas fugaces
                if ((0..100).random() == 0 && shootingStars.size < 3) {
                    val startX = (300..screenWidthPx.toInt()).random().toFloat()
                    val startY = (0..200).random().toFloat()
                    shootingStars = shootingStars + ShootingStar(
                        id = nextId++,
                        position = Offset(startX, startY),
                        velocity = Offset(-20f, 16f),
                        length = (80..160).random().toFloat(),
                        alpha = 1f
                    )
                }

                shootingStars = shootingStars.map { s ->
                    s.copy(
                        position = s.position + s.velocity,
                        alpha = (s.alpha - 0.03f).coerceAtLeast(0f)
                    )
                }.filter { it.alpha > 0f && it.position.x > -150f && it.position.y < screenHeightPx + 150f }

                // 9. Decrementar temporizadores de disparo
                if (playerShootCooldown > 0) playerShootCooldown--
                if (enemyShootCooldown > 0) {
                    enemyShootCooldown--
                } else {
                    // IA del Jefe: Elegir ataque aleatorio y disparar al jugador
                    val attackType = (0..2).random()
                    when (attackType) {
                        0 -> { // Disparo directo rápido
                            val dy = if (playerPos.y > enemyPos.y) 3.5f else if (playerPos.y < enemyPos.y) -3.5f else 0f
                            projectiles = projectiles + Projectile(
                                id = nextId++,
                                position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                velocity = Offset(-13f, dy),
                                isPlayer = false
                            )
                        }
                        1 -> { // Ráfaga Triple en abanico
                            projectiles = projectiles + listOf(
                                Projectile(
                                    id = nextId++,
                                    position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                    velocity = Offset(-11f, -4f),
                                    isPlayer = false
                                ),
                                Projectile(
                                    id = nextId++,
                                    position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                    velocity = Offset(-11f, 0f),
                                    isPlayer = false
                                ),
                                Projectile(
                                    id = nextId++,
                                    position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                    velocity = Offset(-11f, 4f),
                                    isPlayer = false
                                )
                            )
                        }
                        2 -> { // Disparo ondulado (senoidal)
                            projectiles = projectiles + Projectile(
                                id = nextId++,
                                position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                velocity = Offset(-9f, 0f),
                                isPlayer = false,
                                isSineWave = true,
                                startY = enemyPos.y + enemySizePx / 2f
                            )
                        }
                    }
                    enemyShootCooldown = 45 // Cada ~720ms
                }

                // 10. Mover proyectiles en pantalla
                projectiles = projectiles.map { p ->
                    if (p.isSineWave) {
                        val nextAge = p.age + 1
                        val nextX = p.position.x + p.velocity.x
                        val nextY = p.startY + sin(nextAge * 0.15f) * 85f
                        p.copy(position = Offset(nextX, nextY), age = nextAge)
                    } else {
                        p.copy(position = p.position + p.velocity)
                    }
                }.filter { it.position.x in -100f..(screenWidthPx + 100f) && it.position.y in -100f..(screenHeightPx + 100f) }

                // 11. IA del Jefe: Movimiento vertical en el lateral derecho
                val time = System.currentTimeMillis()
                val targetY = hudHeightPx + (sin(time / 450.0).toFloat() * 0.5f + 0.5f) * (screenHeightPx - hudHeightPx - enemySizePx)
                enemyPos = enemyPos.copy(y = targetY)

                // 12. Detección de colisiones precisa usando Rect.overlaps
                val playerRect = Rect(playerPos, Size(playerSizePx, playerSizePx))
                val enemyRect = Rect(enemyPos, Size(enemySizePx, enemySizePx))
                val projPlayerSizePx = with(density) { 16.dp.toPx() }
                val projEnemySizePx = with(density) { 24.dp.toPx() }

                val hitsPlayer = mutableListOf<Projectile>()
                val hitsEnemy = mutableListOf<Projectile>()

                projectiles.forEach { p ->
                    val pSize = if (p.isPlayer) projPlayerSizePx else projEnemySizePx
                    val projRect = Rect(p.position, Size(pSize, pSize))
                    if (p.isPlayer) {
                        if (enemyRect.overlaps(projRect)) {
                            hitsEnemy.add(p)
                        }
                    } else {
                        if (playerRect.overlaps(projRect)) {
                            hitsPlayer.add(p)
                        }
                    }
                }

                if (hitsPlayer.isNotEmpty()) {
                    projectiles = projectiles.filter { it !in hitsPlayer }
                    if (!isInvincible) {
                        playerHealth = (playerHealth - 0.08f).coerceAtLeast(0f)
                        isInvincible = true
                        invincibleFramesLeft = 60 // 1 segundo de invencibilidad y parpadeo
                        
                        // Activar efecto visual de Pop (escala alta)
                        playerScaleTarget = 1.35f
                        playerHitFramesLeft = 8

                        // Sonido de golpe al jugador
                        SoundManager.playPlayerHit()

                        // Añadir texto flotante de impacto
                        damageTexts = damageTexts + DamageText(
                            id = nextId++,
                            text = "💥 -8% HP",
                            position = playerPos.copy(x = playerPos.x + 10f, y = playerPos.y - 20f),
                            alpha = 1f,
                            color = Color(0xFFEF4444)
                        )
                    } else {
                        // Esquiva exitosa debido a inmunidad temporal
                        damageTexts = damageTexts + DamageText(
                            id = nextId++,
                            text = "🛡️ ESQUIVADO",
                            position = playerPos.copy(x = playerPos.x + 10f, y = playerPos.y - 20f),
                            alpha = 1f,
                            color = Color(0xFF06B6D4)
                        )
                    }
                }
                
                if (hitsEnemy.isNotEmpty()) {
                    enemyHealth = (enemyHealth - 0.05f).coerceAtLeast(0f)
                    projectiles = projectiles.filter { it !in hitsEnemy }
                    
                    // Activar efecto visual de Pop (escala alta) para el jefe
                    enemyScaleTarget = 1.25f
                    enemyHitFramesLeft = 8

                    // Sonido de explosión al golpear al jefe
                    SoundManager.playExplosion()

                    // Añadir texto de impacto al jefe
                    damageTexts = damageTexts + DamageText(
                        id = nextId++,
                        text = "⚡ IMPACTO!",
                        position = enemyPos.copy(x = enemyPos.x + (0..60).random() - 30f, y = enemyPos.y - 20f),
                        alpha = 1f,
                        color = Color(0xFFD946EF)
                    )
                }

                if (playerHealth <= 0f) { 
                    isGameOver = true
                    isVictory = false
                    BgmManager.stop() // Apagar música de fondo
                    SoundManager.playDefeat() // Sonido triste de derrota
                }
                if (enemyHealth <= 0f) { 
                    isGameOver = true
                    isVictory = true
                    BgmManager.stop() // Apagar música de fondo
                    SoundManager.playVictory() // Arpegio alegre de victoria
                }
            }
        }

        // --- RENDERIZADO DE FONDO CAPA A CAPA (PARALAJE PROFUNDO) ---

        // 1. Nebulosas gigantes de fondo con degradados radiales ultra suaves
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
                        alpha = 0.18f // Capa difusa trasera
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

        // --- RENDERIZADO DE INTERFAZ Y OBJETOS DEL JUEGO ---

        // HUD (Cabecera superior con barras LED)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            HealthBar(health = playerHealth, label = "PILOTO: IVAN APAZA", color = Color(0xFF06B6D4))
            
            IconButton(
                onClick = onExit,
                modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Close, null, tint = Color.White)
            }
            
            HealthBar(health = enemyHealth, label = "OMEGA BOSS 👾", color = Color(0xFFEF4444))
        }

        // Renderizado de Proyectiles
        projectiles.forEach { p ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(p.position.x.roundToInt(), p.position.y.roundToInt()) }
                    .size(if (p.isPlayer) 16.dp else 24.dp)
                    .shadow(10.dp, CircleShape, ambientColor = if (p.isPlayer) Color.Cyan else Color.Red)
                    .background(
                        Brush.radialGradient(
                            if (p.isPlayer) listOf(Color.Cyan, Color.Transparent) else listOf(Color.Red, Color.Transparent)
                        ),
                        CircleShape
                    )
            )
        }

        // Renderizado del Jugador (Con pulso de invulnerabilidad y escala dinámica pop)
        Box(
            modifier = Modifier
                .offset { IntOffset(playerPos.x.roundToInt(), playerPos.y.roundToInt()) }
                .scale(playerScale)
                .size(playerSizeDp)
                .alpha(if (isInvincible) invincibleAlphaPulse else 1f)
                .shadow(25.dp, CircleShape, ambientColor = Color(0xFF06B6D4))
                .background(Brush.sweepGradient(listOf(Color(0xFF06B6D4), Color(0xFF6366F1))), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🤺", fontSize = 36.sp)
        }

        // Renderizado del Jefe
        val animatedEnemyPos by animateOffsetAsState(targetValue = enemyPos, label = "enemy")
        AnimatedVisibility(
            visible = enemyHealth > 0f,
            enter = fadeIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.offset { IntOffset(animatedEnemyPos.x.roundToInt(), animatedEnemyPos.y.roundToInt()) }
        ) {
            Box(
                modifier = Modifier
                    .scale(enemyScale)
                    .size(enemySizeDp)
                    .shadow(35.dp, RoundedCornerShape(24.dp), ambientColor = Color.Red)
                    .background(Color(0xFF450a0a), RoundedCornerShape(24.dp))
                    .border(3.dp, Color.Red, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("👾", fontSize = 64.sp)
            }
        }

        // Renderizado de Textos Flotantes de Daño
        damageTexts.forEach { dt ->
            Text(
                text = dt.text,
                color = dt.color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .offset { IntOffset(dt.position.x.roundToInt(), dt.position.y.roundToInt()) }
                    .alpha(dt.alpha)
            )
        }

        // Controles Táctiles (Joystick en la izquierda, Botón de disparo en la derecha)
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 48.dp, vertical = 24.dp)) {
            Joystick(
                modifier = Modifier.align(Alignment.BottomStart),
                size = 140f,
                onMove = { x, y ->
                    joystickVector = Offset(x, y)
                }
            )

            Button(
                onClick = { 
                    if (playerShootCooldown == 0) {
                        projectiles = projectiles + Projectile(
                            id = nextId++,
                            position = Offset(playerPos.x + playerSizePx, playerPos.y + playerSizePx / 2f - 8f),
                            velocity = Offset(20f, 0f),
                            isPlayer = true
                        )
                        playerShootCooldown = 8 // Cooldown corto para ráfaga rápida
                        
                        // Sonido láser de disparo del jugador
                        SoundManager.playLaser()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(90.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 15.dp)
            ) {
                Icon(Icons.Default.Bolt, null, modifier = Modifier.size(44.dp))
            }
        }

        // Pantalla de Derrota / Victoria
        if (isGameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isVictory) "¡VICTORIA TOTAL!" else "HAS SIDO DERROTADO",
                        style = MaterialTheme.typography.displayLarge,
                        color = if (isVictory) Color(0xFFD946EF) else Color.Red,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = {
                                playerHealth = 1f
                                enemyHealth = 1f
                                if (screenWidthPx > 0f && screenHeightPx > 0f) {
                                    playerPos = Offset(screenWidthPx * 0.15f, screenHeightPx * 0.5f)
                                    enemyPos = Offset(screenWidthPx * 0.8f, screenHeightPx * 0.5f)
                                } else {
                                    playerPos = Offset(200f, 400f)
                                    enemyPos = Offset(1200f, 400f)
                                }
                                projectiles = emptyList()
                                joystickVector = Offset.Zero
                                playerShootCooldown = 0
                                enemyShootCooldown = 0
                                isInvincible = false
                                invincibleFramesLeft = 0
                                playerScaleTarget = 1f
                                enemyScaleTarget = 1f
                                playerHitFramesLeft = 0
                                enemyHitFramesLeft = 0
                                damageTexts = emptyList()
                                shootingStars = emptyList()
                                isGameOver = false
                                
                                // Reiniciar música de fondo al reintentar
                                BgmManager.start()
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4))
                        ) {
                            Icon(Icons.Default.RestartAlt, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("REINTENTAR")
                        }
                        OutlinedButton(
                            onClick = onExit,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Text("SALIR AL MENÚ", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HealthBar(health: Float, label: String, color: Color) {
    val animatedHealth by animateFloatAsState(targetValue = health, label = "health")
    
    // Animación de pulso LED para el brillo neón
    val ledTransition = rememberInfiniteTransition(label = "ledPulse")
    val ledGlowRadius by ledTransition.animateFloat(
        initialValue = 6f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ledGlow"
    )

    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.shadow(
                elevation = (ledGlowRadius / 2).dp,
                shape = CircleShape,
                ambientColor = color,
                spotColor = color
            )
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(color, color.copy(alpha = 0.2f))
                    ),
                    shape = RoundedCornerShape(9.dp)
                )
                .shadow(
                    elevation = ledGlowRadius.dp,
                    shape = RoundedCornerShape(9.dp),
                    ambientColor = color,
                    spotColor = color
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedHealth)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                color.copy(alpha = 0.5f),
                                Color.White, // Núcleo LED incandescente
                                color
                            )
                        )
                    )
            )
        }
    }
}
