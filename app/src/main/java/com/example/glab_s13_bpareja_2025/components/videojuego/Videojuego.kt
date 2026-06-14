package com.example.glab_s13_bpareja_2025.components.videojuego

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun GamePrototypeScreen() {
    var isPlaying by remember { mutableStateOf(false) }
    var characterPos by remember { mutableStateOf(0.dp) }
    var isAttacking by remember { mutableStateOf(false) }
    var enemyHealth by remember { mutableStateOf(1f) }
    var showEnemy by remember { mutableStateOf(true) }

    val characterOffset by animateDpAsState(
        targetValue = characterPos,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "CharPos"
    )
    val characterScale by animateFloatAsState(
        targetValue = if (isAttacking) 1.5f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "CharScale"
    )
    val healthProgress by animateFloatAsState(
        targetValue = enemyHealth,
        animationSpec = tween(500, easing = LinearOutSlowInEasing),
        label = "Health"
    )

    // Parallax background simulation
    val bgOffset by animateDpAsState(targetValue = characterPos / 3, label = "BgOffset")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF080808))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        if (!isPlaying) {
            Button(
                onClick = { isPlaying = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("INICIAR AVENTURA", color = Color.White, fontWeight = FontWeight.Black)
            }
        } else {
            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = { characterPos -= 20.dp },
                    modifier = Modifier.background(Color(0xFF1E1E1E), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.Cyan)
                }
                
                Button(
                    onClick = {
                        isAttacking = true
                        if (showEnemy) enemyHealth = (enemyHealth - 0.2f).coerceAtLeast(0f)
                        if (enemyHealth <= 0f) showEnemy = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(Icons.Default.Bolt, null)
                    Spacer(Modifier.width(4.dp))
                    Text("ATACAR", fontWeight = FontWeight.ExtraBold)
                }
                
                IconButton(
                    onClick = { characterPos += 20.dp },
                    modifier = Modifier.background(Color(0xFF1E1E1E), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowForward, null, tint = Color.Cyan)
                }
            }
            
            LaunchedEffect(isAttacking) {
                if (isAttacking) {
                    delay(150)
                    isAttacking = false
                }
            }

            // Stage
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0D0D0D))
            ) {
                // Background Grid/Stars (Static Parallax)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = -bgOffset)
                        .background(
                            Brush.linearGradient(
                                0.0f to Color.Transparent,
                                0.5f to Color.White.copy(alpha = 0.02f),
                                1.0f to Color.Transparent
                            )
                        )
                )

                // Character
                Box(
                    modifier = Modifier
                        .offset(x = characterOffset, y = 100.dp)
                        .scale(characterScale)
                        .size(56.dp)
                        .shadow(10.dp, CircleShape, spotColor = Color.Cyan)
                        .background(
                            Brush.sweepGradient(listOf(Color(0xFF03DAC6), Color(0xFF018786))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤺", fontSize = 28.sp)
                }

                // Enemy
                EnemyCharacterUltraModern(
                    visible = showEnemy,
                    health = healthProgress,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp)
                )
                
                // Attack FX
                if (isAttacking) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = characterOffset + 40.dp, y = 10.dp)
                            .size(100.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                    )
                }

                if (!showEnemy) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "VICTORIA",
                            color = Color(0xFFFFD600),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(0.dp),
                                ambientColor = Color.Black
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                showEnemy = true
                                enemyHealth = 1f
                                characterPos = 0.dp
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.RestartAlt, null)
                            Spacer(Modifier.width(4.dp))
                            Text("VOLVER A JUGAR", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnemyCharacterUltraModern(visible: Boolean, health: Float, modifier: Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(1000)) + slideInHorizontally { it },
        exit = fadeOut(tween(500)) + scaleOut(tween(500)),
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { health },
                    modifier = Modifier.size(80.dp),
                    color = Color.Red,
                    strokeWidth = 4.dp,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2D0000))
                        .border(2.dp, Color.Red.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👾", fontSize = 34.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "NIVEL 99",
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black
            )
        }
    }
}
