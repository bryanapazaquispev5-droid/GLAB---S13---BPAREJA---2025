package com.example.glab_s13_bpareja_2025.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNeon,
    secondary = SecondaryCyan,
    tertiary = TertiaryMagenta,
    background = BackgroundDeep,
    surface = SurfaceDark,
    onPrimary = OnSurfaceLight,
    onSecondary = BackgroundDeep,
    onTertiary = OnSurfaceLight,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNeon,
    secondary = SecondaryCyan,
    tertiary = TertiaryMagenta,
    background = OnSurfaceLight,
    surface = Color(0xFFE2E8F0),
    onPrimary = OnSurfaceLight,
    onSecondary = BackgroundDeep,
    onTertiary = OnSurfaceLight,
    onBackground = BackgroundDeep,
    onSurface = BackgroundDeep,
)

@Composable
fun GLABS13BPAREJA2025Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
