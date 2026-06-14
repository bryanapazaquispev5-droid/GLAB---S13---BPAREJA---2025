package com.example.glab_s13_bpareja_2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glab_s13_bpareja_2025.components.color.ColorAnimationScreen
import com.example.glab_s13_bpareja_2025.components.comun.SectionCard
import com.example.glab_s13_bpareja_2025.components.contenido.ContentAnimationScreen
import com.example.glab_s13_bpareja_2025.components.dimensiones.SizeAndPositionAnimationScreen
import com.example.glab_s13_bpareja_2025.components.visibilidad.VisibilityAnimationScreen
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
                    MainScreen(onStartGame = {
                        val intent = Intent(this@MainActivity, GameActivity::class.java)
                        startActivity(intent)
                    })
                }
            }
        }
    }
}

@Composable
fun MainScreen(onStartGame: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    ),
                    radius = 2000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    text = "Animaciones",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "LABORATORIO 13",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
            }

            SectionCard(title = "Ejercicio 1: Visibilidad", icon = Icons.Default.Visibility) {
                VisibilityAnimationScreen()
            }
            
            SectionCard(title = "Ejercicio 2: Transición de Color", icon = Icons.Default.Palette) {
                ColorAnimationScreen()
            }
            
            SectionCard(title = "Ejercicio 3: Tamaño y Posición", icon = Icons.Default.OpenWith) {
                SizeAndPositionAnimationScreen()
            }
            
            SectionCard(title = "Ejercicio 4: Cambio de Contenido", icon = Icons.Default.Sync) {
                ContentAnimationScreen()
            }
            
            SectionCard(title = "Final: Prototipo de Videojuego", icon = Icons.Default.VideogameAsset) {
                Button(
                    onClick = onStartGame,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(8.dp))
                    Text("INICIAR AVENTURA", fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Desarrollado por IVAN APAZA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp)
            )
        }
    }
}
