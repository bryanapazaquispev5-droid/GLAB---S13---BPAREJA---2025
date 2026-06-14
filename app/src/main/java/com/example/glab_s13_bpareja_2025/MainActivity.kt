package com.example.glab_s13_bpareja_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glab_s13_bpareja_2025.ui.theme.GLABS13BPAREJA2025Theme

enum class ScreenState { Loading, Content, Error }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GLABS13BPAREJA2025Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Jetpack Animations",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionCard(title = "Exercise 1: Visibility", icon = Icons.Default.Visibility) {
                VisibilityAnimationScreen()
            }
            
            SectionCard(title = "Exercise 2: Color Transition", icon = Icons.Default.Palette) {
                ColorAnimationScreen()
            }
            
            SectionCard(title = "Exercise 3: Size & Position", icon = Icons.Default.OpenWith) {
                SizeAndPositionAnimationScreen()
            }
            
            SectionCard(title = "Exercise 4: Content Switch", icon = Icons.Default.Sync) {
                ContentAnimationScreen()
            }
            
            SectionCard(title = "Final: Videogame Prototype", icon = Icons.Default.VideogameAsset) {
                GamePrototypeScreen()
            }
            
            SectionCard(title = "Laboratory Observations", icon = Icons.Default.Info) {
                ObservationScreen()
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun VisibilityAnimationScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { isVisible = !isVisible },
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
            Spacer(Modifier.width(8.dp))
            Text(if (isVisible) "Hide Element" else "Show Element")
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(600)) + scaleIn(tween(600)),
            exit = fadeOut(tween(600)) + scaleOut(tween(600))
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                        )
                    )
                    .shadow(8.dp, RoundedCornerShape(24.dp))
            )
        }
    }
}

@Composable
fun ColorAnimationScreen() {
    var isAlternative by remember { mutableStateOf(false) }
    val color1 = MaterialTheme.colorScheme.primary
    val color2 = Color(0xFF4CAF50) // Modern Green
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isAlternative) color2 else color1,
        animationSpec = tween(durationMillis = 800),
        label = "ColorTransition"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { isAlternative = !isAlternative },
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Switch Theme Color")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape)
        )
    }
}

@Composable
fun SizeAndPositionAnimationScreen() {
    var isExpanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (isExpanded) 160.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "Size"
    )
    val offset by animateDpAsState(
        targetValue = if (isExpanded) 40.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Offset"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { isExpanded = !isExpanded },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isExpanded) "Reset Layout" else "Expand Layout")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .offset(x = offset, y = offset)
                    .size(size)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Composable
fun ContentAnimationScreen() {
    var state by remember { mutableStateOf(ScreenState.Loading) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalButton(
            onClick = {
                state = when (state) {
                    ScreenState.Loading -> ScreenState.Content
                    ScreenState.Content -> ScreenState.Error
                    ScreenState.Error -> ScreenState.Loading
                }
            },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cycle Application State")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                },
                label = "ContentState"
            ) { targetState ->
                when (targetState) {
                    ScreenState.Loading -> CircularProgressIndicator(strokeWidth = 6.dp)
                    ScreenState.Content -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Data Synchronized", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                    ScreenState.Error -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Connection Failed", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

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
                    kotlinx.coroutines.delay(150)
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

@Composable
fun ObservationScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ObservationItem(
            label = "The Challenge",
            text = "Coordinating multiple state animations in the videogame prototype, ensuring that 'STRIKE' scaling and enemy health transitions felt responsive."
        )
        ObservationItem(
            label = "AI Assistance",
            text = "Used for UI architectural patterns, modern Material3 styling, and optimizing animation state management."
        )
    }
}

@Composable
fun ObservationItem(label: String, text: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Justify
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GLABS13BPAREJA2025Theme {
        MainScreen()
    }
}