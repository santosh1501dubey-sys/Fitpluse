package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = NeonPulse,
    onPrimary = Color(0xFF003818),
    primaryContainer = Color(0xFF005327),
    onPrimaryContainer = Color(0xFF6BFF9E),
    secondary = PulseCyan,
    onSecondary = Color(0xFF003444),
    secondaryContainer = Color(0xFF004D65),
    onSecondaryContainer = Color(0xFFA5EEFF),
    tertiary = CalorieCoral,
    onTertiary = Color(0xFF490008),
    tertiaryContainer = Color(0xFF700013),
    onTertiaryContainer = Color(0xFFFFB3B8),
    background = DeepDarkBg,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF008744),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8FFFA8),
    onPrimaryContainer = Color(0xFF00210C),
    secondary = Color(0xFF007A99),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBCEBFF),
    onSecondaryContainer = Color(0xFF001F29),
    tertiary = Color(0xFFC91A2A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDAD9),
    onTertiaryContainer = Color(0xFF410007),
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
  )

@Composable
fun FitPulseTheme(
  darkTheme: Boolean = true, // Default to sleek modern dark fitness look
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
