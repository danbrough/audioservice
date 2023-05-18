package danbroid.audioservice.app.ui.theme


import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import danbroid.audioservice.app.R


val LiberationSansFamily = FontFamily(
  Font(R.font.liberation_sans_regular),
  Font(R.font.liberation_sans_bold, FontWeight.Bold),
  Font(R.font.liberation_sans_italic, FontWeight.Normal, FontStyle.Italic),
  Font(R.font.liberation_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
)

val LiberationMonoFamily = FontFamily(
  Font(R.font.liberation_mono_regular),
  Font(R.font.liberation_mono_bold, FontWeight.Bold),
  Font(R.font.liberation_mono_italic, FontWeight.Normal, FontStyle.Italic),
  Font(R.font.liberation_mono_bold_italic, FontWeight.Bold, FontStyle.Italic),
)

val MaterialTheme.monoFontFamily: FontFamily
  get() = FontFamily.Monospace

val Typography = Typography(
  defaultFontFamily = LiberationSansFamily,
  h1 = TextStyle(
    fontWeight = FontWeight.W300,
    fontSize = 96.sp,
    letterSpacing = (-1.5).sp
  ),
  h2 = TextStyle(
    fontWeight = FontWeight.W300,
    fontSize = 60.sp,
    letterSpacing = (-0.5).sp
  ),
  h3 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 48.sp,
    letterSpacing = 0.sp
  ),
  h4 = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 30.sp,
    letterSpacing = 0.sp
  ),
  h5 = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 24.sp,
    letterSpacing = 0.sp
  ),
  h6 = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 20.sp,
    letterSpacing = 0.sp
  ),
  subtitle1 = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
  ),
  subtitle2 = TextStyle(
    fontWeight = FontWeight.W500,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
  ),
  body1 = TextStyle(
    fontWeight = FontWeight.W500,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
  ),
  body2 = TextStyle(
    fontWeight = FontWeight.W500,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
  ),
  button = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
  ),
  caption = TextStyle(
    fontWeight = FontWeight.W500,
    fontSize = 12.sp,
    letterSpacing = 0.4.sp
  ),
  overline = TextStyle(
    fontWeight = FontWeight.W600,
    fontSize = 12.sp,
    letterSpacing = 1.sp
  )
)
