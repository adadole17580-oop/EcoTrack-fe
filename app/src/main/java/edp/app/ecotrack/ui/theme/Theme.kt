package edp.app.ecotrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/*
 * ============================================================
 * DARK THEME SUPPORT COLORS
 * ============================================================
 */

private val ColorDarkBackground = Color(0xFF171A17)
private val ColorDarkSurface = Color(0xFF202420)
private val ColorDarkSurfaceVariant = Color(0xFF2A302A)
private val ColorDarkText = Color(0xFFF1F4F1)
private val ColorDarkSecondaryText = Color(0xFFB5BDB5)
private val ColorDarkBorder = Color(0xFF3B433B)
private val ColorDarkError = Color(0xFFFFB4AB)

/*
 * ============================================================
 * ECOTRACK LIGHT COLOR SCHEME
 * ============================================================
 */

private val EcoTrackLightColorScheme = lightColorScheme(

    primary = EcoGreen,
    onPrimary = EcoWhite,

    primaryContainer = EcoGreenLight,
    onPrimaryContainer = EcoText,

    secondary = EcoGreenDark,
    onSecondary = EcoWhite,

    secondaryContainer = EcoGreenLight,
    onSecondaryContainer = EcoText,

    background = EcoBackground,
    onBackground = EcoText,

    surface = EcoWhite,
    onSurface = EcoText,

    surfaceVariant = EcoGreenLight,
    onSurfaceVariant = EcoSecondaryText,

    outline = EcoBorder,

    error = EcoError,
    onError = EcoWhite,

    errorContainer = EcoErrorLight,
    onErrorContainer = EcoText
)

/*
 * ============================================================
 * ECOTRACK DARK COLOR SCHEME
 * ============================================================
 */

private val EcoTrackDarkColorScheme = darkColorScheme(

    primary = EcoGreenLight,
    onPrimary = EcoGreenDark,

    primaryContainer = EcoGreenDark,
    onPrimaryContainer = EcoWhite,

    secondary = EcoGreenLight,
    onSecondary = EcoGreenDark,

    background = ColorDarkBackground,
    onBackground = ColorDarkText,

    surface = ColorDarkSurface,
    onSurface = ColorDarkText,

    surfaceVariant = ColorDarkSurfaceVariant,
    onSurfaceVariant = ColorDarkSecondaryText,

    outline = ColorDarkBorder,

    error = ColorDarkError,
    onError = ColorDarkBackground
)

/*
 * ============================================================
 * ECOTRACK THEME
 * ============================================================
 */

@Composable
fun EcoTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            EcoTrackDarkColorScheme
        } else {
            EcoTrackLightColorScheme
        },
        typography = Typography,
        content = content
    )
}