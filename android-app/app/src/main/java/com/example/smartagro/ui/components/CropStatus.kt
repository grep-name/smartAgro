package com.example.smartagro.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Custom Colors for better visual impact
val SuccessColor = Color(0xFF00FF09)
val WarningColor = Color(0xFFFFCE3A)

@Composable
fun AnimatedStatusIndicator(
    isTrue: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // --- 1. SUCCESS (Checkmark) ---
        AnimatedVisibility(
            visible = isTrue,
            enter = expandVertically(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            )
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = SuccessColor,
                modifier = Modifier.size(150.dp)
            )
        }

        // --- 2. WARNING (Pulsing Sign) ---
        AnimatedVisibility(
            visible = !isTrue,
            enter = expandVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            )
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")

            // Animating the alpha (transparency) from 1.0 down to 0.4 and back
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 1.0f,
                targetValue = 0.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1300, easing = FastOutSlowInEasing),
                    // Set repeatMode to reverse to pulse back and forth
                ), label = "PulseAlpha"
            )

            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Warning",
                tint = WarningColor,
                modifier = Modifier
                    .size(150.dp)
                    .alpha(pulseAlpha) // Apply the pulsing effect
            )
        }
    }
}


@Composable
fun IrrigationIndicator(
    isTrue: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {

        // --- ✔️ SUCCESS (Pulsing Tick) ---
        AnimatedVisibility(
            visible = isTrue,
            enter = expandVertically(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            )
        ) {

            // Add pulsing only for Tick
            val infiniteTransition = rememberInfiniteTransition(label = "TickPulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 1.0f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "TickAlpha"
            )

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = SuccessColor,
                modifier = Modifier
                    .size(110.dp)
                    .alpha(pulseAlpha) // ✔️ Tick pulses now
            )
        }

        // --- ❌ WARNING (Static Cross) ---
        AnimatedVisibility(
            visible = !isTrue,
            enter = expandVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Warning",
                tint = WarningColor,
                modifier = Modifier.size(110.dp) // static, no alpha animation
            )
        }
    }
}
