package com.example.glab_s13_bpareja_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.glab_s13_bpareja_2025.components.videojuego.GameScreen
import com.example.glab_s13_bpareja_2025.ui.theme.GLABS13BPAREJA2025Theme

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GLABS13BPAREJA2025Theme {
                GameScreen(onExit = { finish() })
            }
        }
    }
}
