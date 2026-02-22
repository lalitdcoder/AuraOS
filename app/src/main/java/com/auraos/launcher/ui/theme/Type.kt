package com.auraos.launcher.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

// Use System fonts to avoid needing downloadable font setup
val AuraTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 57.sp,
        lineHeight = 64.sp,
        color      = AuraTextPrimary
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp,
        lineHeight = 28.sp,
        color      = AuraTextPrimary
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color      = AuraTextPrimary
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color      = AuraOnSurface
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color      = AuraTextSecondary
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color      = AuraCyanLight
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color      = AuraOnSurfaceDim
    )
)
