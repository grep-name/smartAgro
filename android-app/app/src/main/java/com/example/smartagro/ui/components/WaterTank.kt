package com.example.smartagro.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.smartagro.ui.theme.Accent

@Composable
fun WaterTank(
    percentage: Float, // Value between 0.0 and 1.0 (e.g., 0.72f for 72%)
    modifier: Modifier = Modifier,
    waterColor: Color = Color(0xFF3B82F6), // The blue from your image
    borderColor: Color = Color(0xFF87B7FF), // Light grey border
    tankBackgroundColor: Color = Color.White
) {
    // 1. Animate the level for a smooth effect
    val animatedLevel by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "WaterLevelAnimation"
    )

    Canvas(
        modifier = modifier
            .width(80.dp)  // Default size, can be overridden by modifier param
            .height(120.dp)
    ) {
        val width = size.width
        val height = size.height
        val cornerRadius = 12.dp.toPx() // Rounded corners

        // 2. Define the shape of the tank (Rounded Rectangle)
        val tankPath = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height,
                    cornerRadius = CornerRadius(cornerRadius)
                )
            )
        }

        // 3. Clip everything inside this block to the tank shape
        // This ensures the water doesn't spill outside the rounded corners
        clipPath(tankPath) {
            // Draw the white background of the empty tank
            drawRect(color = tankBackgroundColor)

            // Calculate where the water starts (Y-axis)
            // If 0% full, Y = height. If 100% full, Y = 0.
            val waterHeight = height * animatedLevel
            val waterTop = height - waterHeight

            // Draw the Water
            drawRect(
                color = waterColor,
                topLeft = Offset(x = 0f, y = waterTop),
                size = Size(width = width, height = waterHeight)
            )
        }

        // 4. Draw the Border on top
        drawPath(
            path = tankPath,
            color = borderColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}