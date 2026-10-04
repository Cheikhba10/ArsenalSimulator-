package com.arsenalsimulator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(
    primary = Color(0xFF8FA2B8),
    secondary = Color(0xFF6D7A8A),
    background = Color(0xFF0E1114),
    surface = Color(0xFF161A1F),
    onPrimary = Color(0xFF0E1114)
)
private val Light = lightColorScheme(
    primary = Color(0xFF2C3E50),
    secondary = Color(0xFF5A6B7C),
    background = Color(0xFFF5F7FA),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun ArsenalTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, content = content)
}
