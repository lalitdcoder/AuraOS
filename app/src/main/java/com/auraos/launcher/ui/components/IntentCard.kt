package com.auraos.launcher.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auraos.launcher.data.model.IntentHistoryItem
import com.auraos.launcher.data.model.IntentType
import com.auraos.launcher.ui.theme.*

/**
 * A beautiful card that displays a single intent response in the history list.
 */
@Composable
fun IntentCard(
    item: IntentHistoryItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when (item.intentType) {
        IntentType.Calendar -> AuraCyan
        IntentType.Travel   -> AuraPurpleLight
        IntentType.Pizza    -> AuraWarning
        else                -> AuraOutline
    }

    val gradientColors = when (item.intentType) {
        IntentType.Calendar -> listOf(Color(0xFF0C1B33), Color(0xFF0D1B2A))
        IntentType.Travel   -> listOf(Color(0xFF1A0033), Color(0xFF0D0020))
        IntentType.Pizza    -> listOf(Color(0xFF1A0F00), Color(0xFF0D0800))
        else                -> listOf(AuraSurface, AuraSurfaceVariant)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(gradientColors))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(borderColor.copy(alpha = 0.6f), borderColor.copy(alpha = 0.1f))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Emoji icon
                Text(
                    text     = item.response.emoji,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                )

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = item.response.title,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = AuraTextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = item.response.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextSecondary
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Action chip
                        Surface(
                            shape  = RoundedCornerShape(8.dp),
                            color  = borderColor.copy(alpha = 0.15f),
                            modifier = Modifier.border(
                                1.dp, borderColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)
                            )
                        ) {
                            Text(
                                text     = item.response.action,
                                style    = MaterialTheme.typography.labelSmall,
                                color    = borderColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text  = item.response.timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraOnSurfaceDim
                        )
                    }
                    // Original query
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text  = "Query: \"${item.query}\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraOnSurfaceDim
                    )
                }

                // Dismiss button
                IconButton(
                    onClick  = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint   = AuraOnSurfaceDim,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
