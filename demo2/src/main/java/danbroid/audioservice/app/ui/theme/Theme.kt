package danbroid.audioservice.app.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import danbroid.audioservice.app.ui.theme.Elevations
import danbroid.audioservice.app.ui.theme.LocalElevations
import danbroid.util.compose.MaterialColors

/*
    primary: Color = Color(0xFF6200EE),
    primaryVariant: Color = Color(0xFF3700B3),
    secondary: Color = Color(0xFF03DAC6),
    secondaryVariant: Color = Color(0xFF018786),
    background: Color = Color.White,
    surface: Color = Color.White,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.Black,
    onSurface: Color = Color.Black,
    onError: Color = Color.White
 */
val LightThemeColors = lightColors(
  primary = MaterialColors.brown600,
  primaryVariant = MaterialColors.brown900,
  onPrimary = MaterialColors.amber100,

  secondary = MaterialColors.amber600,
  secondaryVariant = MaterialColors.amber900,
  error = MaterialColors.red800,
//  surface = MaterialColors.amber50,
//  onSurface = MaterialColors.red600,

  background = MaterialColors.white,

  )

/*val LightThemeColors = lightColors(
    primary = Purple700,
    primaryVariant = Purple800,
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Red800,
    onError = Color.White
)*/

val DarkThemeColors = darkColors(
  primary = MaterialColors.brown600,
  primaryVariant = MaterialColors.brown900,
  onPrimary = Color.Black,
  secondary = Color.Black,
  onSecondary = Color.White,
  background = Color.Black,
  onBackground = Color.White,
  surface = Color.Black,
  onSurface = Color.White,
  error = MaterialColors.red300,
  onError = Color.Black
)

val Colors.snackbarAction: Color
  @Composable
  get() = if (isLight) LightThemeColors.secondary else DarkThemeColors.secondary

val Colors.progressIndicatorBackground: Color
  @Composable
  get() = if (isLight) Color.Black.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.24f)

@Composable
fun BrewAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
  val colors = if (darkTheme) DarkThemeColors else LightThemeColors
  CompositionLocalProvider(
    LocalElevations provides Elevations()
  ) {
    MaterialTheme(
      colors = colors,
      typography = Typography,
      shapes = Shapes,
      content = content
    )
  }
}


