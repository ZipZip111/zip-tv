package com.ultratv.tv.nativeapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

// ─── Zip-TV — design tokens from zip-dev.ru (styles.css)
// Accent lime #9dff4f on near-black surfaces.

object UltraTokens {
    // Accent — zip-dev.ru brand green
    val Accent       = Color(0xFF9DFF4F)
    val Accent2      = Color(0xFF7ACC3A)
    val AccentGlow   = Color(0x8C9DFF4F)
    val AccentSoft   = Color(0x269DFF4F)  // ~15% — pill highlights
    val AccentTint   = Color(0x1A9DFF4F)  // ~10% — gradient stops
    val AccentGhost  = Color(0x059DFF4F)  // ~2% — card gradient end
    val AccentBorder = Color(0x409DFF4F)  // ~25% — card borders
    val AccentBorderMedium = Color(0x4D9DFF4F)
    val AccentBorderStrong = Color(0x669DFF4F)

    // Status
    val Live = Color(0xFF9DFF4F)
    val Hd   = Color(0xFF9DFF4F)
    val Uhd  = Color(0xFFB8FF7A)
    val Ok   = Color(0xFF9DFF4F)
    val Warn = Color(0xFFFFB547)

    // Neutrals — zip-dev.ru dark palette
    val Fg  = Color(0xFFF5F5F5)
    val Fg2 = Color(0xFFC7C7CF)
    val Fg3 = Color(0xFFA6A6A6)
    val Fg4 = Color(0xFF5A5A64)

    val Line  = Color(0x142A2A2A)
    val Line2 = Color(0x402A2A2A)

    // Surfaces — overlays on top of bg-0
    val Surface1      = Color(0x0AFFFFFF) // 4%
    val Surface2      = Color(0x0FFFFFFF) // 6%
    val Surface3      = Color(0x14FFFFFF) // 8%
    val SurfaceStrong = Color(0x24FFFFFF) // 14%
    val Scrim         = Color(0x73000000) // 45%
    val ScrimStrong   = Color(0xD9000000) // 85%

    // CTA
    val CtaBg        = Color(0xFF9DFF4F)
    val CtaFgOnCta   = Color(0xFF0A0A0A)

    // Radii
    val RadiusXs: Dp = 6.dp
    val RadiusSm: Dp = 10.dp
    val RadiusMd: Dp = 14.dp
    val RadiusLg: Dp = 20.dp
    val RadiusXl: Dp = 28.dp

    // Layout
    val SidebarCollapsed: Dp = 92.dp
    val SidebarExpanded:  Dp = 220.dp
    val TopBarHeight:     Dp = 76.dp
    val EdgeGutter:       Dp = 80.dp
    val LeftEdge:         Dp = 92.dp
}

object UltraFonts {
    val Sans:  FontFamily = FontFamily.SansSerif
    val Mono:  FontFamily = FontFamily.Monospace
    val Serif: FontFamily = FontFamily.Serif
}

@androidx.compose.runtime.Composable
@androidx.tv.material3.ExperimentalTvMaterial3Api
fun ultraCardColors(
    containerColor: androidx.compose.ui.graphics.Color = UltraTokens.Surface1,
    contentColor: androidx.compose.ui.graphics.Color = UltraTokens.Fg,
    focusedContainerColor: androidx.compose.ui.graphics.Color = UltraTokens.AccentSoft,
    focusedContentColor: androidx.compose.ui.graphics.Color = UltraTokens.Fg,
): androidx.tv.material3.CardColors = androidx.tv.material3.CardDefaults.colors(
    containerColor = containerColor,
    contentColor = contentColor,
    focusedContainerColor = focusedContainerColor,
    focusedContentColor = focusedContentColor,
    pressedContainerColor = focusedContainerColor,
    pressedContentColor = focusedContentColor,
)

@androidx.compose.runtime.Composable
@androidx.tv.material3.ExperimentalTvMaterial3Api
fun ultraButtonColors(
    containerColor: androidx.compose.ui.graphics.Color = UltraTokens.Surface2,
    contentColor: androidx.compose.ui.graphics.Color = UltraTokens.Fg,
    focusedContainerColor: androidx.compose.ui.graphics.Color = UltraTokens.Accent,
    focusedContentColor: androidx.compose.ui.graphics.Color = UltraTokens.CtaFgOnCta,
): androidx.tv.material3.ButtonColors = androidx.tv.material3.ButtonDefaults.colors(
    containerColor = containerColor,
    contentColor = contentColor,
    focusedContainerColor = focusedContainerColor,
    focusedContentColor = focusedContentColor,
    pressedContainerColor = focusedContainerColor,
    pressedContentColor = focusedContentColor,
)

object UltraType {
    val HeroDisplay = TextStyle(
        fontFamily = UltraFonts.Serif,
        fontSize = 84.sp,
        lineHeight = 84.sp,
        letterSpacing = (-2.1).sp,
        fontWeight = FontWeight.Normal,
    )
    val ScreenTitle = TextStyle(
        fontFamily = UltraFonts.Sans,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
        fontWeight = FontWeight.SemiBold,
    )
    val RailTitle = TextStyle(
        fontFamily = UltraFonts.Sans,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.3).sp,
        fontWeight = FontWeight.SemiBold,
    )
    val SerifTitle = TextStyle(
        fontFamily = UltraFonts.Serif,
        fontSize = 28.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp,
        fontStyle = FontStyle.Normal,
    )

    val Body  = TextStyle(fontFamily = UltraFonts.Sans, fontSize = 15.sp, lineHeight = 22.sp)
    val Body2 = TextStyle(fontFamily = UltraFonts.Sans, fontSize = 13.sp, lineHeight = 18.sp)
    val Meta  = TextStyle(fontFamily = UltraFonts.Sans, fontSize = 12.sp, lineHeight = 16.sp, color = UltraTokens.Fg3)
    val Eyebrow = TextStyle(
        fontFamily = UltraFonts.Sans,
        fontSize = 13.sp,
        letterSpacing = 2.3.sp,
        fontWeight = FontWeight.Medium,
        color = UltraTokens.Fg3,
    )
    val Mono = TextStyle(fontFamily = UltraFonts.Mono, fontSize = 13.sp)
    val MonoSmall = TextStyle(fontFamily = UltraFonts.Mono, fontSize = 11.sp, color = UltraTokens.Fg3)
}
