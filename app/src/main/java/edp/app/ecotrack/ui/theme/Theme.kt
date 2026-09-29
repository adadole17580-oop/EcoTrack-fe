package edp.app.ecotrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView

private val EcoTrackColorScheme = lightColorScheme(

    primary = EcoGreen,

    onPrimary = EcoWhite,

    primaryContainer = EcoSoftGreen,

    onPrimaryContainer = EcoDarkGreen,

    secondary = EcoTextGreen,

    onSecondary = EcoWhite,

    secondaryContainer = EcoLightGreen,

    onSecondaryContainer = EcoDarkGreen,

    background = EcoLightGreen,

    onBackground = Color(0xFF222222),

    surface = EcoWhite,

    onSurface = Color(0xFF222222),

    surfaceVariant = EcoPaleGreen,

    onSurfaceVariant = EcoGray,

    outline = EcoBorderGreen
)

@Composable
fun EcoTrackTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = EcoTrackColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EcoTrackTypography,
        content = content
    )
}