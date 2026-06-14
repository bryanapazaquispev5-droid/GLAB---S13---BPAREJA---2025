package com.example.glab_s13_bpareja_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.components.*
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GLABS13BPAREJA2025Theme {
        MainScreen()
    }
}