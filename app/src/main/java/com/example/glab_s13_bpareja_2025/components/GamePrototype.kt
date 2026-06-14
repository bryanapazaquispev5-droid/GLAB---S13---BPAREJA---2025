package com.example.glab_s13_bpareja_2025.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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

    val characterOffset by animateDpAsState(targetValue = characterPos, label = "CharPos")
    val characterScale by animateFloatAsState(
        targetValue = if (isAttacking) 1.4f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "CharScale"
    )
    val healthProgress by animateFloatAsState(targetValue = enemyHealth, label = "Health")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        if (!isPlaying) {
            Button(
                onClick = { isPlaying = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB86FC))
            ) {
                Text("ENTER COMBAT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(onClick = { characterPos -= 15.dp }, modifier = Modifier.background(Color.DarkGray, CircleShape)) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Button(
                    onClick = {
                        isAttacking = true
                        if (showEnemy) enemyHealth = (enemyHealth - 0.25f).coerceAtLeast(0f)
                        if (enemyHealth <= 0f) showEnemy = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("STRIKE", fontWeight = FontWeight.ExtraBold)
                }
                IconButton(onClick = { characterPos += 15.dp }, modifier = Modifier.background(Color.DarkGray, CircleShape)) {
                    Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                }
            }
            
            LaunchedEffect(isAttacking) {
                if (isAttacking) {
                    delay(150)
                    isAttacking = false
                }
            }

            Box(modifier = Modifier.height(200.dp).fillMaxWidth().padding(top = 24.dp)) {
                // Character
                Box(
                    modifier = Modifier
                        .offset(x = characterOffset, y = 80.dp)
                        .scale(characterScale)
                        .size(48.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color(0xFF03DAC6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚔️", fontSize = 20.sp)
                }

                // Enemy logic
                EnemyCharacterModern(
                    visible = showEnemy,
                    health = healthProgress,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
                )
                
                if (!showEnemy) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("VICTORY", color = Color(0xFFFFD700), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                        TextButton(onClick = {
                            showEnemy = true
                            enemyHealth = 1f
                            characterPos = 0.dp
                        }) {
                            Text("RETRY", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnemyCharacterModern(visible: Boolean, health: Float, modifier: Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally(),
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("BOSS", color = Color.Red, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { health },
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.Red,
                trackColor = Color.DarkGray
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFB00020))
                    .border(2.dp, Color.Red, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("👹", fontSize = 32.sp)
            }
        }
    }
}
