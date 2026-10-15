package edp.app.ecotrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EcoTrackLightColorScheme = lightColorScheme(
    primary = EcoGreen,
    onPrimary = EcoWhite,
    primaryContainer = EcoSoftGreen,
    onPrimaryContainer = EcoDarkGreen,
    secondary = EcoTextGreen,
    onSecondary = EcoWhite,
    secondaryContainer = EcoLightGreen,
    onSecondaryContainer = EcoDarkGreen,
    background = EcoLightGreen,
    onBackground = Color(0xFF1B261D),
    surface = EcoWhite,
    onSurface = Color(0xFF1B261D),
    surfaceVariant = EcoPaleGreen,
    onSurfaceVariant = EcoGray,
    outline = EcoBorderGreen
)

private val EcoTrackDarkColorScheme = darkColorScheme(
    primary = EcoGreen,
    onPrimary = EcoWhite,
    primaryContainer = Color(0xFF1B3B23),
    onPrimaryContainer = Color(0xFFAEE5C2),
    secondary = Color(0xFF4CAE70),
    onSecondary = Color(0xFF121814),
    secondaryContainer = Color(0xFF1A3322),
    onSecondaryContainer = Color(0xFFC7EFCF),
    background = Color(0xFF121814),
    onBackground = Color(0xFFE2E4DF),
    surface = Color(0xFF1C251E),
    onSurface = Color(0xFFE2E4DF),
    surfaceVariant = Color(0xFF26332A),
    onSurfaceVariant = Color(0xFFB0BEB3),
    outline = Color(0xFF3E5043)
)

@Composable
fun EcoTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        EcoTrackDarkColorScheme
    } else {
        EcoTrackLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EcoTrackTypography,
        content = content
    )
}