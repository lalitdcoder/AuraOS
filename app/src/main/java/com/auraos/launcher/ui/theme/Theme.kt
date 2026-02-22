package com.auraos.launcher.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AuraDarkColorScheme = darkColorScheme(
    primary            = AuraPurpleLight,
    onPrimary          = AuraOnPrimary,
    primaryContainer   = AuraPurpleMid,
    onPrimaryContainer = AuraOnSurface,
    secondary          = AuraCyan,
    onSecondary        = AuraOnPrimary,
    secondaryContainer = AuraCyanDark,
    onSecondaryContainer = AuraOnSurface,
    tertiary           = AuraTeal,
    onTertiary         = AuraOnPrimary,
    background         = AuraPurpleDeep,
    onBackground       = AuraTextPrimary,
    surface            = AuraSurface,
    onSurface          = AuraOnSurface,
    surfaceVariant     = AuraSurfaceVariant,
    onSurfaceVariant   = AuraTextSecondary,
    outline            = AuraOutline,
    error              = AuraError,
    onError            = AuraOnPrimary
)

@Composable
fun AuraOSTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = AuraDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AuraPurpleDeep.toArgb()
            window.navigationBarColor = AuraPurpleDeep.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars    = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AuraTypography,
        content     = content
    )
}
