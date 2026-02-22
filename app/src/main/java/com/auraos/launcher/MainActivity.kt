package com.auraos.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.auraos.launcher.ui.screens.HomeScreen
import com.auraos.launcher.ui.theme.AuraOSTheme
import com.auraos.launcher.viewmodel.MainViewModel

/**
 * Main (and only) Activity for Aura OS.
 *
 * Declared with HOME & LAUNCHER intent-filter in AndroidManifest so the
 * system presents Aura OS as the default home screen selector.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the content draw edge-to-edge behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AuraOSTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }

    // Prevent Aura OS from being backgrounded with the back button
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Intentionally do nothing — home screens should not back out
    }
}
