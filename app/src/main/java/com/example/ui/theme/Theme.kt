package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = EmeraldMedium,
    onPrimary = Color.White,
    primaryContainer = EmeraldSubtle,
    onPrimaryContainer = EmeraldDeep,
    secondary = GoldPrimary,
    onSecondary = Color.White,
    secondaryContainer = GoldLight,
    onSecondaryContainer = Color(0xFF533B05),
    tertiary = TerracottaAccent,
    onTertiary = Color.White,
    background = SandBackground,
    onBackground = CharcoalText,
    surface = SandSurface,
    onSurface = CharcoalText,
    surfaceVariant = SandSurfaceVariant,
    onSurfaceVariant = MutedText,
    outline = Color(0xFFD6CFC7)
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldWarm,
    onPrimary = Color(0xFF221600),
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = EmeraldSubtle,
    secondary = GoldPrimary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3B2A08),
    onSecondaryContainer = GoldLight,
    tertiary = TerracottaAccent,
    onTertiary = Color.White,
    background = Color(0xFF0D1612),
    onBackground = Color(0xFFECE7E1),
    surface = Color(0xFF131E19),
    onSurface = Color(0xFFECE7E1),
    surfaceVariant = Color(0xFF1B2A23),
    onSurfaceVariant = Color(0xFFA5B2AC),
    outline = Color(0xFF384941)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted hotel palette
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
