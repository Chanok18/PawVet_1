package com.example.pawvet_1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    // Eliminamos las referencias a Container y Variant que ya no existen
    primaryContainer = Primary,
    onPrimaryContainer = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Secondary,
    onSecondaryContainer = OnSecondary,
    tertiary = Tertiary,
    onTertiary = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = Background,
    onSurfaceVariant = OnBackground,
    error = Error,
    onError = OnError
)

@Composable
fun PawVet_1Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}