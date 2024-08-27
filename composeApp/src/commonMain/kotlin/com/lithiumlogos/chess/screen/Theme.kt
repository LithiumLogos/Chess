package com.lithiumlogos.chess.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = BlueGrey80,
    onPrimary = BlueGrey20,
    primaryContainer = BlueGrey30,
    onPrimaryContainer = BlueGrey90,
    inversePrimary = BlueGrey40,
    secondary = Desert80,
    onSecondary = Desert20,
    secondaryContainer = Desert30,
    onSecondaryContainer = Desert90,
    tertiary = Copper80,
    onTertiary = Copper20,
    tertiaryContainer = Copper30,
    onTertiaryContainer = Copper90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = SandGrey10,
    onBackground = SandGrey90,
    surface = SandGrey80,
    onSurface = SandGrey20,
    inverseSurface = SandGrey90,
    inverseOnSurface = SandGrey10,
    surfaceVariant = SandGrey80
)

private val LightColorPalette = lightColorScheme(
    primary = BlueGrey40,
    onPrimary = BlueGrey10,
    primaryContainer = BlueGrey20,
    onPrimaryContainer = BlueGrey80,
    inversePrimary = BlueGrey30,
    secondary = Desert20,
    onSecondary = Desert80,
    secondaryContainer = Desert40,
    onSecondaryContainer = Desert10,
    tertiary = Copper20,
    onTertiary = Copper80,
    tertiaryContainer = Copper40,
    onTertiaryContainer = Copper10,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = SandGrey90,
    onBackground = SandGrey10,
    surface = SandGrey20,
    onSurface = SandGrey80,
    inverseSurface = SandGrey10,
    inverseOnSurface = SandGrey90,
    surfaceVariant = SandGrey20
)

@Composable
fun Material3AppTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = Shapes(),
        content = content
    )
}
