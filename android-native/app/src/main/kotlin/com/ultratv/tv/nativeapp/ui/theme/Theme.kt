package com.ultratv.tv.nativeapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import com.ultratv.tv.nativeapp.data.prefs.AppTheme

private val Amoled = darkColorScheme(
    primary = UltraTokens.Accent,
    onPrimary = UltraTokens.CtaFgOnCta,
    background = Color(0xFF080808),
    onBackground = UltraTokens.Fg,
    surface = Color(0xFF111111),
    onSurface = UltraTokens.Fg,
    surfaceVariant = Color(0xFF151515),
    onSurfaceVariant = UltraTokens.Fg2,
    border = UltraTokens.Line,
    inverseSurface = Color(0xFF1D1D1D),
    inverseOnSurface = Color(0xFF0A0A0A),
)

private val Dark = darkColorScheme(
    primary = UltraTokens.Accent,
    onPrimary = UltraTokens.CtaFgOnCta,
    background = Color(0xFF0A0A0A),
    onBackground = UltraTokens.Fg,
    surface = Color(0xFF111111),
    onSurface = UltraTokens.Fg,
    surfaceVariant = Color(0xFF151515),
    onSurfaceVariant = UltraTokens.Fg2,
    border = UltraTokens.Line,
    inverseSurface = Color(0xFF1D1D1D),
    inverseOnSurface = Color(0xFF0A0A0A),
)

private val Light = lightColorScheme(
    primary = UltraTokens.Accent,
    onPrimary = UltraTokens.CtaFgOnCta,
    background = Color(0xFFF4F3EF),
    onBackground = Color(0xFF14110E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF14110E),
    surfaceVariant = Color(0xFFEBEAE5),
    onSurfaceVariant = Color(0xFF3D3A36),
    border = Color(0x1A14110E),
    inverseSurface = Color(0xFF14110E),
    inverseOnSurface = Color.White,
)

private val Blue = darkColorScheme(
    primary = UltraTokens.Accent,
    onPrimary = UltraTokens.CtaFgOnCta,
    background = Color(0xFF080808),
    onBackground = UltraTokens.Fg,
    surface = Color(0xFF111111),
    onSurface = UltraTokens.Fg,
    surfaceVariant = Color(0xFF151515),
    onSurfaceVariant = UltraTokens.Fg2,
    border = UltraTokens.Line,
    inverseSurface = Color(0xFF1D1D1D),
    inverseOnSurface = Color(0xFF0A0A0A),
)

@Composable
fun UltraTvTheme(theme: AppTheme = AppTheme.AMOLED, content: @Composable () -> Unit) {
    val scheme = when (theme) {
        AppTheme.DARK -> Dark
        AppTheme.AMOLED -> Amoled
        AppTheme.BLUE -> Blue
        AppTheme.LIGHT -> Light
    }
    MaterialTheme(colorScheme = scheme, content = content)
}
