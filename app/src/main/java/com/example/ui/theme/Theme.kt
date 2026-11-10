package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val JhanviDarkColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceContainer,
    onPrimaryContainer = TextPrimary,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = ElectricCyan,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CardGlowBorder
)

// In case light theme is toggled by user, provide a high-contrast futuristic violet theme
private val JhanviLightColorScheme = lightColorScheme(
    primary = Color(0xFF7A1FA2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E5F5),
    onPrimaryContainer = Color(0xFF4A148C),
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    background = Color(0xFFFAF7FD),
    onBackground = Color(0xFF1E102F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E102F),
    surfaceVariant = Color(0xFFEDE7F6),
    onSurfaceVariant = Color(0xFF4A148C)
)

@Composable
fun JhanviTheme(
    darkTheme: Boolean = true, // Default to futuristic dark purple theme as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) JhanviDarkColorScheme else JhanviLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
