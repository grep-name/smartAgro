package com.example.smartagro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ProbeData(
    val id: Int,
    val moisture: Float, // 0.0 to 1.0
    val temp: Int
)

@Composable
fun ProbeCapsule(
    data: ProbeData,
    modifier: Modifier = Modifier
) {
    // Determine color based on Temp (Simple Logic)
    val tempColor = when {
        data.moisture < 0.3f -> Color(0xFFEF4444)
        data.moisture > 0.6f -> Color(0xFF10B981)
        else -> Color(0xFFFFC107)
    }

    // Animation for the liquid level
    val animatedLevel by animateFloatAsState(
        targetValue = data.moisture,
        animationSpec = tween(1000),
        label = "level"
    )

    Card(
        modifier = modifier.height(160.dp), // Fixed height for the capsule look
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- LAYER 1: The Liquid Canvas ---
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerRadius = 24.dp.toPx()
                val width = size.width
                val height = size.height

                // 1. Define the Capsule Shape
                val capsulePath = Path().apply {
                    addRoundRect(
                        androidx.compose.ui.geometry.RoundRect(
                            left = 0f, top = 0f, right = width, bottom = height,
                            cornerRadius = CornerRadius(cornerRadius)
                        )
                    )
                }

                // 2. Clip to shape and draw content
                clipPath(capsulePath) {
                    // Background (Empty part of tube)
                    drawRect(color = Color(0xFFF3F4F6))

                    // The Liquid (Moisture)
                    val liquidHeight = height * animatedLevel

                    // Gradient fill for modern look
                    val liquidBrush = Brush.verticalGradient(
                        colors = listOf(tempColor.copy(alpha = 0.6f), tempColor),
                        startY = height - liquidHeight,
                        endY = height
                    )

                    drawRect(
                        brush = liquidBrush,
                        topLeft = Offset(0f, height - liquidHeight),
                        size = Size(width, liquidHeight)
                    )
                }
            }

            // --- LAYER 2: The Text Info Overlay ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Probe Number
                Text(
                    text = "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                // Middle: Moisture % (White text if submerged, dark if not - simple trick)
                // For simplicity, we keep it centered and high contrast
                Text(
                    text = "${(data.moisture * 100).toInt()}%",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = if (data.moisture > 0.5f) Color.White else Color.Black.copy(0.7f)
                )

                // Bottom: Temp
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${data.temp}°c",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (data.moisture > 0.9f) Color.White else Color.Black.copy(0.8f)
                    )
                }
            }
        }
    }
}