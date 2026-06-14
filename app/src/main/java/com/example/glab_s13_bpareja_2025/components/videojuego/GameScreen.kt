package com.example.glab_s13_bpareja_2025.components.videojuego

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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

@Composable
fun GameScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    
    // Bloquear a Horizontal programáticamente como seguridad adicional
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
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

        val playerSizeDp = 70.dp
        val enemySizeDp = 120.dp
        val playerSizePx = with(density) { playerSizeDp.toPx() }
        val enemySizePx = with(density) { enemySizeDp.toPx() }

        val hudHeightDp = 100.dp
        val hudHeightPx = with(density) { hudHeightDp.toPx() }

        var playerPos by remember { mutableStateOf(Offset(200f, 400f)) }
        var enemyPos by remember { mutableStateOf(Offset(1200f, 400f)) }
        
        // Posicionar relativamente cuando las dimensiones del mapa estén listas
        LaunchedEffect(screenWidthPx, screenHeightPx) {
            if (screenWidthPx > 0f && screenHeightPx > 0f) {
                playerPos = Offset(screenWidthPx * 0.15f, screenHeightPx * 0.5f)
                enemyPos = Offset(screenWidthPx * 0.8f, screenHeightPx * 0.5f)
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

                // 3. Mover y atenuar textos de daño flotantes
                damageTexts = damageTexts.map {
                    it.copy(
                        position = it.position.copy(y = it.position.y - 2.5f),
                        alpha = (it.alpha - 0.03f).coerceAtLeast(0f)
                    )
                }.filter { it.alpha > 0f }

                // 4. Decrementar temporizadores de disparo
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

                // 5. Mover proyectiles en pantalla
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

                // 6. IA del Jefe: Movimiento vertical en el lateral derecho
                val time = System.currentTimeMillis()
                val targetY = hudHeightPx + (sin(time / 450.0).toFloat() * 0.5f + 0.5f) * (screenHeightPx - hudHeightPx - enemySizePx)
                enemyPos = enemyPos.copy(y = targetY)

                // 7. Detección de colisiones precisa usando Rect.overlaps
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
                    
                    // Añadir texto de impacto al jefe
                    damageTexts = damageTexts + DamageText(
                        id = nextId++,
                        text = "⚡ IMPACTO!",
                        position = enemyPos.copy(x = enemyPos.x + (0..60).random() - 30f, y = enemyPos.y - 20f),
                        alpha = 1f,
                        color = Color(0xFFD946EF)
                    )
                }

                if (playerHealth <= 0f) { isGameOver = true; isVictory = false }
                if (enemyHealth <= 0f) { isGameOver = true; isVictory = true }
            }
        }

        // Estrellas de fondo aleatorias
        repeat(35) { index ->
            val starX = remember(index) { (Math.random() * 2000).toFloat().dp }
            val starY = remember(index) { (Math.random() * 1000).toFloat().dp }
            val starSize = remember(index) { (1..3).random().dp }
            val starAlpha = remember(index) { (0.15f + Math.random() * 0.3f).toFloat() }
            Box(
                modifier = Modifier
                    .offset(x = starX, y = starY)
                    .size(starSize)
                    .background(Color.White.copy(alpha = starAlpha), CircleShape)
            )
        }

        // HUD (Cabecera superior)
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

        // Renderizado del Jugador (Con pulso de invulnerabilidad / parpadeo de daño)
        Box(
            modifier = Modifier
                .offset { IntOffset(playerPos.x.roundToInt(), playerPos.y.roundToInt()) }
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
                                damageTexts = emptyList()
                                isGameOver = false
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
    Column(horizontalAlignment = Alignment.Start) {
        Text(label, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedHealth)
                    .background(Brush.horizontalGradient(listOf(color.copy(alpha = 0.7f), color)))
            )
        }
    }
}
