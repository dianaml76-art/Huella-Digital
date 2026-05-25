package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightAdultColorScheme = lightColorScheme(
    primary = AdultoPrincipal,
    secondary = AdultoSecundario,
    tertiary = AdultoAcento,
    background = AdultoFondo,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1A2130),
    onSurface = Color(0xFF1A2130),
    surfaceVariant = Color(0xFFE8EEF5)
)

private val LightKidColorScheme = lightColorScheme(
    primary = NiñosPrincipal,
    secondary = NiñosSecundario,
    tertiary = NiñosAcento,
    background = NiñosFondo,
    surface = Color(0xFFFBFBFF),
    onPrimary = Color.White,
    onSecondary = Color(0xFF423B80),
    onTertiary = Color(0xFF423B80),
    onBackground = Color(0xFF2A245C),
    onSurface = Color(0xFF2A245C),
    surfaceVariant = Color(0xFFF0EFFF)
)

private val CustomDarkColorScheme = darkColorScheme(
    primary = OscuroPrincipal,
    secondary = OscuroSecundario,
    tertiary = OscuroAcento,
    background = OscuroFondo,
    surface = OscuroSuperficie,
    onPrimary = Color(0xFF11111B),
    onSecondary = Color(0xFF11111B),
    onTertiary = Color(0xFF11111B),
    onBackground = Color(0xFFCDD6F4),
    onSurface = Color(0xFFCDD6F4),
    surfaceVariant = Color(0xFF45475A)
)

@Composable
fun HuellaSeguraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    profile: String? = null, // "adult" o "child"
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> CustomDarkColorScheme
        profile == "child" -> LightKidColorScheme
        else -> LightAdultColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
