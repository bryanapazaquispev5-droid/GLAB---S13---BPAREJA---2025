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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun GameScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    
    // Lock to landscape and start background music (BGM)
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        BgmManager.start()
        onDispose {
            BgmManager.stop()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    // Screen shake state
    var screenShakeTimer by remember { mutableStateOf(0) }
    
    val shakeOffset = if (screenShakeTimer > 0) {
        Offset(
            (-12..12).random().toFloat(),
            (-12..12).random().toFloat()
        )
    } else {
        Offset.Zero
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(shakeOffset.x.roundToInt(), shakeOffset.y.roundToInt()) }
            .background(Color(0xFF020617))
    ) {
        val density = LocalDensity.current
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }

        val screenWidthDp = maxWidth
        val screenHeightDp = maxHeight

        val playerSizeDp = 75.dp
        val enemySizeDp = 125.dp
        val playerSizePx = with(density) { playerSizeDp.toPx() }
        val enemySizePx = with(density) { enemySizeDp.toPx() }

        val hudHeightDp = 100.dp
        val hudHeightPx = with(density) { hudHeightDp.toPx() }

        val projPlayerSizePx = with(density) { 16.dp.toPx() }
        val projEnemySizePx = with(density) { 24.dp.toPx() }

        var playerPos by remember { mutableStateOf(Offset(200f, 400f)) }
        var enemyPos by remember { mutableStateOf(Offset(1200f, 400f)) }

        // Dynamic background space elements (parallax)
        var scrollingStars by remember { mutableStateOf(emptyList<BackgroundStar>()) }
        var nebulae by remember { mutableStateOf(emptyList<NebulaCloud>()) }
        var asteroids by remember { mutableStateOf(emptyList<SpaceAsteroid>()) }
        var shootingStars by remember { mutableStateOf(emptyList<ShootingStar>()) }
        
        // High score loading
        var highScore by remember { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            highScore = HighScoreManager.getHighScore(context)
        }

        // Relative positions when map dimensions are ready
        LaunchedEffect(screenWidthPx, screenHeightPx) {
            if (screenWidthPx > 0f && screenHeightPx > 0f) {
                playerPos = Offset(screenWidthPx * 0.15f, screenHeightPx * 0.5f)
                enemyPos = Offset(screenWidthPx * 0.8f, screenHeightPx * 0.5f)

                // Initialize 3-layer parallax scrolling stars
                scrollingStars = List(45) { i ->
                    val size = if (i < 20) {
                        (1.0f + Math.random() * 0.5f).toFloat() // Far layer
                    } else if (i < 35) {
                        (1.8f + Math.random() * 0.7f).toFloat() // Mid layer
                    } else {
                        (2.8f + Math.random() * 1.0f).toFloat() // Near layer
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

                // Initialize 3 huge, slow nebulas
                nebulae = listOf(
                    NebulaCloud(0L, Offset(screenWidthPx * 0.15f, screenHeightPx * 0.1f), 300f, Color(0xFFD946EF), 0.12f),
                    NebulaCloud(1L, Offset(screenWidthPx * 0.55f, screenHeightPx * 0.4f), 360f, Color(0xFF6366F1), 0.08f),
                    NebulaCloud(2L, Offset(screenWidthPx * 0.82f, screenHeightPx * 0.65f), 280f, Color(0xFF06B6D4), 0.15f)
                )

                // Initialize asteroid belt
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

        // Dash, invulnerability, and floating numbers
        var isInvincible by remember { mutableStateOf(false) }
        var invincibleFramesLeft by remember { mutableStateOf(0) }
        var damageTexts by remember { mutableStateOf(emptyList<DamageText>()) }

        // Spring animation targets for character impact pop
        var playerScaleTarget by remember { mutableStateOf(1f) }
        var enemyScaleTarget by remember { mutableStateOf(1f) }
        val playerScale by animateFloatAsState(targetValue = playerScaleTarget, label = "playerScale")
        val enemyScale by animateFloatAsState(targetValue = enemyScaleTarget, label = "enemyScale")

        var playerHitFramesLeft by remember { mutableStateOf(0) }
        var enemyHitFramesLeft by remember { mutableStateOf(0) }

        // Additional props
        var particles by remember { mutableStateOf(emptyList<Particle>()) }
        var powerUps by remember { mutableStateOf(emptyList<PowerUp>()) }
        var shieldActive by remember { mutableStateOf(false) }
        var doubleShotTimer by remember { mutableStateOf(0) }
        var score by remember { mutableStateOf(0) }
        var comboCount by remember { mutableStateOf(0) }
        var comboResetTimer by remember { mutableStateOf(0) }
        var isBossRage by remember { mutableStateOf(false) }
        var time by remember { mutableStateOf(0L) }

        // Infinitely pulsing alpha during player invulnerability
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

        // Game Loop (~60 FPS)
        LaunchedEffect(isGameOver, screenWidthPx, screenHeightPx) {
            if (screenWidthPx <= 0f || screenHeightPx <= 0f) return@LaunchedEffect
            while (!isGameOver) {
                delay(16)
                time++

                // 1. Move player via Joystick with physics bounds
                if (joystickVector != Offset.Zero) {
                    val speed = 14f
                    val nextX = (playerPos.x + joystickVector.x * speed).coerceIn(0f, screenWidthPx - playerSizePx)
                    val nextY = (playerPos.y + joystickVector.y * speed).coerceIn(hudHeightPx, screenHeightPx - playerSizePx)
                    playerPos = Offset(nextX, nextY)
                }

                // 2. Manage invulnerability frames
                if (invincibleFramesLeft > 0) {
                    invincibleFramesLeft--
                    if (invincibleFramesLeft == 0) {
                        isInvincible = false
                    }
                }

                // 3. Manage pop scale frame times
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

                // 4. Update floating combat texts
                damageTexts = damageTexts.map {
                    it.copy(
                        position = it.position.copy(y = it.position.y - 2.5f),
                        alpha = (it.alpha - 0.03f).coerceAtLeast(0f)
                    )
                }.filter { it.alpha > 0f }

                // 5. Parallax scroll stars
                scrollingStars = scrollingStars.map { s ->
                    var nextX = s.position.x - s.speed
                    if (nextX < -30f) {
                        nextX = screenWidthPx + 30f
                    }
                    s.copy(position = Offset(nextX, s.position.y))
                }

                // 6. Scroll giant gaseous nebulas
                nebulae = nebulae.map { n ->
                    var nextX = n.position.x - n.speed
                    val sizePx = with(density) { n.size.dp.toPx() }
                    if (nextX < -sizePx) {
                        nextX = screenWidthPx + 50f
                    }
                    n.copy(position = Offset(nextX, n.position.y))
                }

                // 7. Scroll & rotate asteroids
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

                // 8. Update shooting stars
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

                // 9. Update spark explosion particles
                particles = particles.map { p ->
                    p.copy(
                        position = p.position + p.velocity,
                        age = p.age + 1
                    )
                }.filter { it.age < it.maxAge }

                // 10. Update & spawn floating power-ups
                if (powerUps.isEmpty() && (0..750).random() == 0) {
                    val pType = if (Math.random() < 0.5) PowerUpType.SHIELD else PowerUpType.DOUBLE_SHOT
                    val startY = hudHeightPx + (Math.random() * (screenHeightPx - hudHeightPx - 100f)).toFloat()
                    powerUps = powerUps + PowerUp(
                        id = nextId++,
                        position = Offset(screenWidthPx + 50f, startY),
                        type = pType,
                        size = 50f,
                        speed = 3.5f
                    )
                }
                powerUps = powerUps.map { p ->
                    p.copy(position = p.position.copy(x = p.position.x - p.speed))
                }.filter { it.position.x > -100f }

                // 11. Handle Player & Power-Up collisions
                val playerRect = Rect(playerPos, Size(playerSizePx, playerSizePx))
                val collected = mutableListOf<PowerUp>()
                powerUps.forEach { p ->
                    val pRect = Rect(p.position, Size(p.size, p.size))
                    if (playerRect.overlaps(pRect)) {
                        collected.add(p)
                        SoundManager.playPowerUp()
                        when (p.type) {
                            PowerUpType.SHIELD -> {
                                shieldActive = true
                                damageTexts = damageTexts + DamageText(
                                    id = nextId++,
                                    text = "🛡️ ESCUDO ACTIVADO",
                                    position = playerPos.copy(x = playerPos.x - 20f, y = playerPos.y - 30f),
                                    alpha = 1.2f,
                                    color = Color(0xFF06B6D4)
                                )
                            }
                            PowerUpType.DOUBLE_SHOT -> {
                                doubleShotTimer = 480 // 8 seconds at 60 FPS
                                damageTexts = damageTexts + DamageText(
                                    id = nextId++,
                                    text = "⚡ DOBLE LÁSER ACTIVADO",
                                    position = playerPos.copy(x = playerPos.x - 20f, y = playerPos.y - 30f),
                                    alpha = 1.2f,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }
                }
                if (collected.isNotEmpty()) {
                    powerUps = powerUps.filter { it !in collected }
                }

                // 12. Double shot timer
                if (doubleShotTimer > 0) doubleShotTimer--

                // 13. Combo reset timer
                if (comboResetTimer > 0) {
                    comboResetTimer--
                    if (comboResetTimer == 0) {
                        comboCount = 0
                    }
                }

                // 14. Screen shake timer
                if (screenShakeTimer > 0) screenShakeTimer--

                // 15. Decrement shoot cooldowns
                if (playerShootCooldown > 0) playerShootCooldown--
                if (enemyShootCooldown > 0) {
                    enemyShootCooldown--
                } else {
                    // Boss AI: Shoot dynamic bullets (accelerated/diagonal in Rage Mode)
                    val attackType = if (isBossRage) (0..3).random() else (0..2).random()
                    when (attackType) {
                        0 -> { // Quick direct shot
                            val dy = if (playerPos.y > enemyPos.y) 3.5f else if (playerPos.y < enemyPos.y) -3.5f else 0f
                            projectiles = projectiles + Projectile(
                                id = nextId++,
                                position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                velocity = Offset(if (isBossRage) -17f else -13f, dy),
                                isPlayer = false
                            )
                        }
                        1 -> { // Triple fan burst
                            projectiles = projectiles + listOf(
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, -5f), false),
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, 0f), false),
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, 5f), false)
                            )
                        }
                        2 -> { // Wavy sine shot
                            projectiles = projectiles + Projectile(
                                id = nextId++,
                                position = Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f),
                                velocity = Offset(-9f, 0f),
                                isPlayer = false,
                                isSineWave = true,
                                startY = enemyPos.y + enemySizePx / 2f
                            )
                        }
                        3 -> { // Rage attack: 4-way diagonal spray
                            projectiles = projectiles + listOf(
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, -8f), false),
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, -4f), false),
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, 4f), false),
                                Projectile(nextId++, Offset(enemyPos.x - 30f, enemyPos.y + enemySizePx / 2f), Offset(-12f, 8f), false)
                            )
                        }
                    }
                    enemyShootCooldown = if (isBossRage) 22 else 45
                }

                // 16. Move active bullets
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

                // 17. Boss AI: vertical movement oscillating
                val targetY = hudHeightPx + (sin(time / (if (isBossRage) 300.0 else 450.0)).toFloat() * 0.5f + 0.5f) * (screenHeightPx - hudHeightPx - enemySizePx)
                enemyPos = enemyPos.copy(y = targetY)

                // 18. Exact Rect-overlap collision detection
                val enemyRect = Rect(enemyPos, Size(enemySizePx, enemySizePx))
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
                        if (shieldActive) {
                            shieldActive = false
                            isInvincible = true
                            invincibleFramesLeft = 40
                            playerScaleTarget = 1.2f
                            playerHitFramesLeft = 6
                            
                            particles = particles + ParticleSystem.createExplosion(nextId, hitsPlayer.first().position, Color.Cyan, 8)
                            nextId += 8
                            
                            SoundManager.playExplosion()
                            damageTexts = damageTexts + DamageText(
                                id = nextId++,
                                text = "🛡️ ESCUDO ROTO",
                                position = playerPos.copy(x = playerPos.x + 10f, y = playerPos.y - 20f),
                                alpha = 1f,
                                color = Color.Cyan
                            )
                        } else {
                            // Player takes increased damage (12% per hit for a higher difficulty challenge!)
                            playerHealth = (playerHealth - 0.12f).coerceAtLeast(0f)
                            isInvincible = true
                            invincibleFramesLeft = 60
                            playerScaleTarget = 1.35f
                            playerHitFramesLeft = 8
                            comboCount = 0 // Break combo

                            particles = particles + ParticleSystem.createExplosion(nextId, hitsPlayer.first().position, Color(0xFF06B6D4), 12)
                            nextId += 12

                            SoundManager.playPlayerHit()
                            damageTexts = damageTexts + DamageText(
                                id = nextId++,
                                text = "💥 -12% HP",
                                position = playerPos.copy(x = playerPos.x + 10f, y = playerPos.y - 20f),
                                alpha = 1f,
                                color = Color(0xFFEF4444)
                            )
                            
                            screenShakeTimer = 10
                        }
                    } else {
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
                    // Player bullets do less damage (1% per hit instead of 2% for a longer, more challenging battle!)
                    enemyHealth = (enemyHealth - 0.01f).coerceAtLeast(0f)
                    projectiles = projectiles.filter { it !in hitsEnemy }
                    
                    enemyScaleTarget = 1.25f
                    enemyHitFramesLeft = 8

                    comboCount++
                    comboResetTimer = 120 // 2 seconds
                    val hitScore = 100 * comboCount
                    score += hitScore

                    particles = particles + ParticleSystem.createExplosion(nextId, hitsEnemy.first().position, Color(0xFFD946EF), 10)
                    nextId += 10

                    SoundManager.playExplosion()

                    if (comboCount >= 2) {
                        damageTexts = damageTexts + DamageText(
                            id = nextId++,
                            text = "Combo x$comboCount! +$hitScore",
                            position = enemyPos.copy(x = enemyPos.x - 30f, y = enemyPos.y - 40f),
                            alpha = 1f,
                            color = Color(0xFFFFD700)
                        )
                    } else {
                        damageTexts = damageTexts + DamageText(
                            id = nextId++,
                            text = "⚡ IMPACTO!",
                            position = enemyPos.copy(x = enemyPos.x + (0..60).random() - 30f, y = enemyPos.y - 20f),
                            alpha = 1f,
                            color = Color(0xFFD946EF)
                        )
                    }

                    // Activate boss rage mode when health <= 50%
                    if (enemyHealth <= 0.5f && !isBossRage) {
                        isBossRage = true
                        BgmManager.isRageMode = true
                        damageTexts = damageTexts + DamageText(
                            id = nextId++,
                            text = "🚨 ¡ALERTA: MODO FURIA! 🚨",
                            position = Offset(screenWidthPx * 0.3f, screenHeightPx * 0.35f),
                            alpha = 2f,
                            color = Color.Red
                        )
                    }
                }

                if (playerHealth <= 0f) { 
                    isGameOver = true
                    isVictory = false
                    BgmManager.stop()
                    SoundManager.playDefeat()
                }
                if (enemyHealth <= 0f) { 
                    isGameOver = true
                    isVictory = true
                    BgmManager.stop()
                    
                    // Save High Score
                    HighScoreManager.saveHighScore(context, score)
                    highScore = HighScoreManager.getHighScore(context)
                    
                    SoundManager.playVictory()
                }
            }
        }

        // RENDER SPATIAL PARALLAX BACKGROUND
        GameBackground(
            nebulae = nebulae,
            scrollingStars = scrollingStars,
            asteroids = asteroids,
            shootingStars = shootingStars,
            screenWidthDp = screenWidthDp,
            screenHeightDp = screenHeightDp
        )

        // RENDER FLOAT POWER-UPS
        powerUps.forEach { p ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(p.position.x.roundToInt(), p.position.y.roundToInt()) }
                    .size(42.dp)
                    .shadow(12.dp, CircleShape, ambientColor = if (p.type == PowerUpType.SHIELD) Color.Cyan else Color.Yellow)
                    .background(
                        Brush.radialGradient(
                            if (p.type == PowerUpType.SHIELD) listOf(Color(0xFF06B6D4), Color(0xFF020617)) else listOf(Color(0xFFFFB300), Color(0xFF020617))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, if (p.type == PowerUpType.SHIELD) Color.Cyan else Color.Yellow, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (p.type == PowerUpType.SHIELD) "🛡️" else "⚡", fontSize = 20.sp)
            }
        }

        // RENDER DUST PARTICLES
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                val progress = p.age.toFloat() / p.maxAge
                val size = 6f * (1f - progress)
                val alpha = 1f - progress
                drawCircle(
                    color = p.color.copy(alpha = alpha),
                    radius = size,
                    center = p.position
                )
            }
        }

        // HUD OVERLAYS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            HealthBar(health = playerHealth, label = "PILOTO: IVAN APAZA", color = Color(0xFF06B6D4))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onExit,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text("SCORE: $score", color = Color(0xFFFFD700), fontSize = 16.sp, fontWeight = FontWeight.Black)
                Text("RECORD: $highScore", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                if (comboCount >= 2) {
                    Text("COMBO x$comboCount", color = Color(0xFFD946EF), fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            }
            
            HealthBar(health = enemyHealth, label = "OMEGA BOSS 👾", color = Color(0xFFEF4444))
        }

        // RENDER BULLETS
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

        // SHIELD CIRCLE OUTLINE
        if (shieldActive) {
            Box(
                modifier = Modifier
                    .offset { IntOffset((playerPos.x - 10f).roundToInt(), (playerPos.y - 10f).roundToInt()) }
                    .size((playerSizeDp.value + 20).dp)
                    .border(3.dp, Brush.sweepGradient(listOf(Color.Cyan, Color.Transparent, Color.Cyan)), CircleShape)
                    .shadow(15.dp, CircleShape, ambientColor = Color.Cyan)
            )
        }

        // RENDER PLAYER (With dynamic scale bounce & Lottie JSON Character animation)
        Box(
            modifier = Modifier
                .offset { IntOffset(playerPos.x.roundToInt(), playerPos.y.roundToInt()) }
                .scale(playerScale)
                .size(playerSizeDp)
                .alpha(if (isInvincible) invincibleAlphaPulse else 1f)
                .shadow(25.dp, CircleShape, ambientColor = Color(0xFF06B6D4)),
            contentAlignment = Alignment.Center
        ) {
            PlayerLottieCharacter(modifier = Modifier.fillMaxSize())
        }

        // RENDER BOSS (With Lottie JSON Character animation)
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
                    .shadow(35.dp, RoundedCornerShape(24.dp), ambientColor = Color.Red),
                contentAlignment = Alignment.Center
            ) {
                EnemyLottieCharacter(modifier = Modifier.fillMaxSize())
            }
        }

        // RENDER FLOATING DAMAGE TEXTS
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

        // HUD CONTROLS (Joystick Left, Shoot Right)
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
                        if (doubleShotTimer > 0) {
                            // Double parallel lasers
                            projectiles = projectiles + listOf(
                                Projectile(
                                    id = nextId++,
                                    position = Offset(playerPos.x + playerSizePx, playerPos.y + playerSizePx * 0.15f),
                                    velocity = Offset(22f, 0f),
                                    isPlayer = true
                                ),
                                Projectile(
                                    id = nextId++,
                                    position = Offset(playerPos.x + playerSizePx, playerPos.y + playerSizePx * 0.75f),
                                    velocity = Offset(22f, 0f),
                                    isPlayer = true
                                )
                            )
                        } else {
                            // Simple laser
                            projectiles = projectiles + Projectile(
                                id = nextId++,
                                position = Offset(playerPos.x + playerSizePx, playerPos.y + playerSizePx / 2f - 8f),
                                velocity = Offset(20f, 0f),
                                isPlayer = true
                            )
                        }
                        playerShootCooldown = 8 // short cooldown for fast firing
                        
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

        // DEFEAT / VICTORY OVERLAYS
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "PUNTUACIÓN FINAL: $score",
                        color = Color(0xFFFFD700),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    if (score >= highScore && score > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "🏆 ¡NUEVO RÉCORD DE PUNTUACIÓN! 🏆",
                            color = Color(0xFF00E676),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
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
                                
                                particles = emptyList()
                                powerUps = emptyList()
                                shieldActive = false
                                doubleShotTimer = 0
                                score = 0
                                comboCount = 0
                                comboResetTimer = 0
                                isBossRage = false
                                screenShakeTimer = 0
                                time = 0L

                                isGameOver = false
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
