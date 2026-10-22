package dev.apslop.apps.cafeteria_review.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB1551B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBC8),
    onPrimaryContainer = Color(0xFF3B1200),
    secondary = Color(0xFF6D5844),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF8DEC8),
    onSecondaryContainer = Color(0xFF271A0C),
    tertiary = Color(0xFF3D6B4F),
    background = Color(0xFFFFFBFF),
    surface = Color(0xFFFFFBFF),
    surfaceVariant = Color(0xFFF3DFD4),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB68A),
    onPrimary = Color(0xFF5F1E00),
    primaryContainer = Color(0xFF7E3A0F),
    onPrimaryContainer = Color(0xFFFFDBC8),
    secondary = Color(0xFFDCC2A8),
    onSecondary = Color(0xFF3B2A18),
    secondaryContainer = Color(0xFF54432F),
    onSecondaryContainer = Color(0xFFF8DEC8),
    tertiary = Color(0xFFA6D4B4),
    background = Color(0xFF1C1108),
    surface = Color(0xFF1C1108),
    surfaceVariant = Color(0xFF4F4440),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
