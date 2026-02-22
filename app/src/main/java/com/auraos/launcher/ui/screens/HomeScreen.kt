package com.auraos.launcher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auraos.launcher.data.model.IntentType
import com.auraos.launcher.ui.components.IntentCard
import com.auraos.launcher.ui.components.PizzaAnimation
import com.auraos.launcher.ui.theme.*
import com.auraos.launcher.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Animated infinite orb pulse for background effect
    val infiniteTransition = rememberInfiniteTransition(label = "bgOrb")
    val orbAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue  = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbAlpha"
    )
    val orbScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue  = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientTop, GradientMid, GradientBottom),
                    startY = 0f,
                    endY   = Float.POSITIVE_INFINITY
                )
            )
    ) {
        // Background decorative orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Purple orb (top-left)
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(AuraPurple.copy(alpha = orbAlpha), Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.2f),
                    radius = size.width * 0.5f * orbScale
                ),
                center = Offset(size.width * 0.2f, size.height * 0.2f),
                radius = size.width * 0.5f * orbScale
            )
            // Cyan orb (bottom-right)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuraCyan.copy(alpha = orbAlpha * 0.6f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.75f),
                    radius = size.width * 0.4f
                ),
                center = Offset(size.width * 0.8f, size.height * 0.75f),
                radius = size.width * 0.4f
            )
        }

        // Main content
        Column(
            modifier            = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ─── Header ────────────────────────────────────────────────────────
            Spacer(Modifier.height(56.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Icon(
                    imageVector      = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint             = AuraCyanLight,
                    modifier         = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text       = "AURA",
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Black,
                    color      = AuraTextPrimary,
                    letterSpacing = 8.sp
                )
            }
            Text(
                text     = "Intent-Based Intelligence",
                fontSize = 12.sp,
                color    = AuraCyan,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(32.dp))

            // ─── Pizza Animation overlay ────────────────────────────────────────
            AnimatedVisibility(
                visible = uiState.currentAnimation is IntentType.Pizza,
                enter   = fadeIn() + slideInVertically { -40 },
                exit    = fadeOut() + slideOutVertically { -40 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(AuraSurface.copy(alpha = 0.9f))
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(AuraWarning.copy(0.5f), AuraPurpleLight.copy(0.3f))),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    PizzaAnimation(
                        onAnimationEnd = { viewModel.clearAnimation() }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // ─── Input Field ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(AuraSurfaceVariant.copy(0.85f), AuraSurface.copy(0.9f))
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(AuraPurpleLight.copy(0.5f), AuraCyan.copy(0.3f))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.inputText,
                        onValueChange = viewModel::onInputChanged,
                        placeholder = {
                            Text(
                                "What should Aura do?",
                                color = AuraOnSurfaceDim,
                                fontSize = 15.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor       = AuraTextPrimary,
                            unfocusedTextColor     = AuraTextPrimary,
                            cursorColor            = AuraCyanLight,
                            focusedBorderColor     = Color.Transparent,
                            unfocusedBorderColor   = Color.Transparent,
                            focusedContainerColor  = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                viewModel.onSubmit()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        enabled    = !uiState.isProcessing
                    )

                    // Send / Loading button
                    AnimatedContent(
                        targetState = uiState.isProcessing,
                        label       = "sendBtn"
                    ) { processing ->
                        if (processing) {
                            CircularProgressIndicator(
                                modifier  = Modifier.size(32.dp),
                                color     = AuraCyanLight,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(
                                onClick  = {
                                    viewModel.onSubmit()
                                    keyboardController?.hide()
                                },
                                enabled  = uiState.inputText.isNotBlank()
                            ) {
                                Icon(
                                    imageVector      = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint             = if (uiState.inputText.isNotBlank()) AuraCyanLight else AuraOnSurfaceDim
                                )
                            }
                        }
                    }
                }
            }

            // ─── Intent History ─────────────────────────────────────────────────
            if (uiState.intentHistory.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Intent History",
                        style      = MaterialTheme.typography.labelLarge,
                        color      = AuraCyanLight,
                        letterSpacing = 1.sp
                    )
                    TextButton(onClick = viewModel::clearHistory) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = "Clear",
                            tint   = AuraOnSurfaceDim,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Clear", color = AuraOnSurfaceDim, fontSize = 12.sp)
                    }
                }

                LazyColumn(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.intentHistory,
                        key   = { it.id }
                    ) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter   = fadeIn() + slideInVertically { -30 }
                        ) {
                            IntentCard(
                                item      = item,
                                onDismiss = { viewModel.dismissHistoryItem(item.id) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            } else {
                // Empty state
                Spacer(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("✨", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text      = "Ask Aura anything",
                        color     = AuraTextSecondary,
                        fontSize  = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text      = "Try: \"Calendar\", \"Travel\" or \"Pizza\"",
                        color     = AuraOnSurfaceDim,
                        fontSize  = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }
    }
}
