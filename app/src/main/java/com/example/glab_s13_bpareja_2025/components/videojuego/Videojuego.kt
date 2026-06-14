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
import com.example.glab_s13_bpareja_2025.components.comun.AnimatedButton
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
        targetValue = if (isAttacking) 1.6f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "CharScale"
    )
    val healthProgress by animateFloatAsState(
        targetValue = enemyHealth,
        animationSpec = tween(500, easing = LinearOutSlowInEasing),
        label = "Health"
    )

    val bgOffset by animateDpAsState(targetValue = characterPos / 4, label = "BgOffset")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF020617))
            .border(2.dp, Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFFD946EF))), RoundedCornerShape(32.dp))
            .padding(16.dp)
    ) {
        if (!isPlaying) {
            AnimatedButton(
                onClick = { isPlaying = true },
                containerColor = Color(0xFF6366F1)
            ) {
                Text("INICIAR AVENTURA", fontWeight = FontWeight.Black)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = { characterPos -= 25.dp },
                    modifier = Modifier.background(Color(0xFF1E293B), CircleShape).size(56.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color(0xFF06B6D4))
                }
                
                AnimatedButton(
                    onClick = {
                        isAttacking = true
                        if (showEnemy) enemyHealth = (enemyHealth - 0.25f).coerceAtLeast(0f)
                        if (enemyHealth <= 0f) showEnemy = false
                    },
                    containerColor = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Bolt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("ATACAR", fontWeight = FontWeight.ExtraBold)
                }
                
                IconButton(
                    onClick = { characterPos += 25.dp },
                    modifier = Modifier.background(Color(0xFF1E293B), CircleShape).size(56.dp)
                ) {
                    Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF06B6D4))
                }
            }
            
            LaunchedEffect(isAttacking) {
                if (isAttacking) {
                    delay(150)
                    isAttacking = false
                }
            }

            Box(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F172A))
            ) {
                // Cyber Background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = -bgOffset)
                        .background(
                            Brush.linearGradient(
                                0.0f to Color.Transparent,
                                0.5f to Color(0xFF6366F1).copy(alpha = 0.05f),
                                1.0f to Color.Transparent
                            )
                        )
                )

                // Character
                Box(
                    modifier = Modifier
                        .offset(x = characterOffset, y = 110.dp)
                        .scale(characterScale)
                        .size(64.dp)
                        .shadow(15.dp, CircleShape, ambientColor = Color(0xFF06B6D4))
                        .background(
                            Brush.sweepGradient(listOf(Color(0xFF06B6D4), Color(0xFF6366F1))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤺", fontSize = 32.sp)
                }

                // Enemy
                EnemyCharacterCyber(
                    visible = showEnemy,
                    health = healthProgress,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp)
                )
                
                if (isAttacking) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = characterOffset + 45.dp, y = 20.dp)
                            .size(120.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color.White.copy(alpha = 0.4f), Color.Transparent)
                                )
                            )
                    )
                }

                if (!showEnemy) {
                    Column(
                        modifier = Modifier.align(Alignment.Center).fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "VICTORIA",
                            color = Color(0xFFD946EF),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(Modifier.height(12.dp))
                        TextButton(
                            onClick = {
                                showEnemy = true
                                enemyHealth = 1f
                                characterPos = 0.dp
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.RestartAlt, null)
                            Spacer(Modifier.width(8.dp))
                            Text("REINTENTAR", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnemyCharacterCyber(visible: Boolean, health: Float, modifier: Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(1000)) + slideInHorizontally { it },
        exit = fadeOut(tween(500)) + scaleOut(tween(800)),
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { health },
                    modifier = Modifier.size(90.dp),
                    color = Color(0xFFEF4444),
                    strokeWidth = 6.dp,
                    trackColor = Color.White.copy(alpha = 0.05f)
                )
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF450a0a))
                        .border(2.dp, Color(0xFFEF4444), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👾", fontSize = 38.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "BOSS LVL 99",
                color = Color(0xFFEF4444),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
        }
    }
}
