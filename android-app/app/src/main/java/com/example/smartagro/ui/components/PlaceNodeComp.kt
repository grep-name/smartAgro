package com.example.smartagro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PulsingStatusIndicator(
    size: Dp = 50.dp,
    minScale: Float = 0.8f,
    maxScale: Float = 1.1f,
    durationMillis: Int = 1000,
    pulseColor: Color = MaterialTheme.colorScheme.primary
) {
    // 1. Define the animation state
    val scale by animateFloatAsState(
        // Toggle between minScale and maxScale
        targetValue = if (remember { mutableStateOf(false) }.apply {
                LaunchedEffect(Unit) {
                    // Infinitely toggle the state to drive the animation
                    while (true) {
                        delay(durationMillis.toLong())
                        value = !value
                    }
                }
            }.value) maxScale else minScale,

        // 2. Define the animation specification (infinite loop)
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse // Scales up and then scales down smoothly
        ),
        label = "PulsingScaleAnimation"
    )

    // 3. The circular Box with the scale transformation applied
    Box(
        modifier = Modifier
            .size(size) // Set the base size
            .graphicsLayer {
                // Apply the animated scale to the Box
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape) // Make it perfectly circular
            .background(pulseColor), // Set the color
        contentAlignment = Alignment.Center
    ){}
}