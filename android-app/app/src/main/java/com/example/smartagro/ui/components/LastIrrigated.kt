package com.example.smartagro.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Define theme colors based on your previous image
val FarmBlue = Color(0xFF3B82F6)
val FarmTeal = Color(0xFF10B981) // A complementary teal for accent
val FarmLightBg = Color(0xFFEFF6FF) // Very light blue background

@SuppressLint("UnusedBoxWithConstraintsScope")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LastIrrigatedBadge(
    timestamp: String,
    modifier: Modifier = Modifier, // Pass the fillMaxWidth/aspectRatio here
    primaryColor: Color = FarmBlue,
    accentColor: Color = FarmTeal
) {
    // Animation Setup (Same as before)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.2f, // Reduced scale slightly to stay in bounds
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "alpha"
    )

    // KEY FIX: Use BoxWithConstraints to know how big we are allowed to be
    BoxWithConstraints(
        modifier = modifier, // This now takes the aspect ratio from the parent
        contentAlignment = Alignment.Center
    ) {
        // Calculate the radius dynamically based on the available width
        val w = maxWidth
        val h = maxHeight
        // Use the smaller dimension to ensure it fits
        val minDimension = minOf(w, h)

        // Canvas fills the box
        Canvas(modifier = Modifier.size(minDimension)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val mainRadius = (size.width / 2) * 0.85f // Leave 20% room for the pulse

            // 1. Pulse
            drawCircle(
                color = accentColor.copy(alpha = pulseAlpha),
                radius = mainRadius * pulseScale,
                center = center
            )
            // 2. Main Ring
            drawCircle(
                brush = Brush.sweepGradient(listOf(primaryColor, accentColor, primaryColor)),
                radius = mainRadius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
            // 3. Inner Fill
            drawCircle(
                color = primaryColor.copy(alpha = 0.05f),
                radius = mainRadius - 4.dp.toPx(),
                center = center,
            )
        }

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painterResource(com.example.smartagro.R.drawable.clock), null, tint = Color.Gray, modifier = Modifier.size(34.dp))
                Spacer(Modifier.height(2.dp))
                Text(timestamp, fontSize = 28.sp, color = Color.Gray)
            }
        }
    }
}


// --- Helper function for java.time formatting (requires API 26+) ---
@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(instant: Instant): Pair<String, String> {
    val istZoneId = ZoneId.of("Asia/Kolkata") // "Asia/Kolkata" is the canonical ID for IST

    // 2. Convert the Instant to ZonedDateTime in IST
    val zonedDateTime = instant.atZone(istZoneId)

    // Example format: "Oct 24"
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.US)

    // Example format: "2:30 PM"
    // Note: Using Locale.US ensures the 'a' (AM/PM) marker is consistent and in English.
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    return Pair(
        dateFormatter.format(zonedDateTime),
        // Convert the time string to lowercase as requested
        timeFormatter.format(zonedDateTime).lowercase(Locale.US)
    )
}