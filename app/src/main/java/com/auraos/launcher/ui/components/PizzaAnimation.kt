package com.auraos.launcher.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auraos.launcher.ui.theme.AuraTextPrimary
import com.auraos.launcher.ui.theme.AuraTextSecondary
import kotlinx.coroutines.delay

/**
 * Pizza ordering animation — pulsing emoji with status text.
 * Perfect example of how Aura makes simple interactions feel alive.
 */
@Composable
fun PizzaAnimation(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    var step by remember { mutableIntStateOf(0) }

    val statusMessages = listOf(
        "🍕  Placing order...",
        "✅  Order confirmed!",
        "👨‍🍳  Chef is cooking...",
        "🛵  Out for delivery!",
        "🎉  Pizza is on its way!"
    )

    // Auto-advance steps
    LaunchedEffect(Unit) {
        for (i in statusMessages.indices) {
            step = i
            delay(900L)
        }
        delay(600L)
        onAnimationEnd()
    }

    // Heartbeat scale animation
    val scale by animateFloatAsState(
        targetValue = if (step % 2 == 0) 1.0f else 1.15f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessLow
        ),
        label = "pizzaScale"
    )

    Column(
        modifier            = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text      = "🍕",
            fontSize  = 64.sp,
            modifier  = Modifier.scale(scale)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text      = if (step < statusMessages.size) statusMessages[step] else statusMessages.last(),
            fontSize  = 18.sp,
            color     = AuraTextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = "Estimated delivery: 35 min",
            fontSize = 13.sp,
            color = AuraTextSecondary
        )
    }
}
