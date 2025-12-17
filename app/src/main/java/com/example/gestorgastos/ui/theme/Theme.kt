package com.example.gestorgastos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimaryDark,
    secondary = PurplePrimaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkBackground, // El texto dentro del botón morado será oscuro
    onBackground = WhiteText,
    onSurface = WhiteText
)

// 2. Definimos el esquema CLARO
private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurplePrimary,
    background = WhiteBackground,
    surface = LightSurface,
    onPrimary = WhiteBackground,
    onBackground = BlackText,
    onSurface = BlackText
)

@Composable
fun GestorGastosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // (Opcional) Cambia el color de la barra de estado (donde sale la batería)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Barra del mismo color que el fondo
            // Si el tema es oscuro, los iconos de la barra (hora, wifi) deben ser claros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}