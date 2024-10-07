package he2b.be.startreview.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = darkPrimary,
    onPrimary = purpleGrey,
    background = darkBackground,
    onBackground = darkOnBackground,
    onError = darkOnError,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkOnSurfaceVariant,
    outline = darkOutline,
    surface = darkSurface,
    onSurface = darkOnSurface,
    secondaryContainer = darkSecondaryContainer
)

private val LightColorScheme = lightColorScheme(
    primary = lightPrimary,
    secondary = purpleGrey,
    background = lightBackground,
    onBackground = lightOnBackground,
    onError = lightOnError,
    surfaceVariant = lightSurfaceVariant,
    onSurfaceVariant = lightOnSurfaceVariant,
    outline = lightOutline,
    surface = lightSurface,
    onSurface = lightOnSurface,
    secondaryContainer = lightSecondaryContainer
)

@Composable
fun StartReviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}