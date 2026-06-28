package com.example.smartagro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.viewmodel.KisanViewModel

data class FieldState(
    val switchId: Int, // E.g., 1 for the left pair, 2 for the right pair
    val isFlowing: Boolean // NOTE: This is now the local state representation
)

@Composable
fun FarmWaterControlScreen() {
    val KisanViewModel: KisanViewModel = viewModel()
    val data by KisanViewModel.kisanData.collectAsState()

    // State management for the two switch groups (Local State)
    // Initialize with data from RTDB directly
    var fieldStates by remember {
        mutableStateOf(
            listOf(
                FieldState(1, isFlowing = data.Valve1), // INIT with RTDB
                FieldState(2, isFlowing = data.Valve2)  // INIT with RTDB
            )
        )
    }

    // 🚨 FIX 1: Synchronize local state with remote data whenever 'data' changes.
    // This ensures the initial state is set and external changes update the UI.
    LaunchedEffect(data) {
        fieldStates = fieldStates.map { state ->
            when (state.switchId) {
                1 -> state.copy(isFlowing = data.Valve1)
                2 -> state.copy(isFlowing = data.Valve2)
                else -> state
            }
        }
    }

    // 🚨 FIX 2: Function to toggle the flow state (MUST NOW WRITE TO RTDB)
    fun toggleFlow(switchId: Int) {
        // Use the live ViewModel data to determine the current state
        val currentFlowing = if (switchId == 1) data.Valve1 else data.Valve2
        val newState = !currentFlowing

        // Write the new state to the RTDB/ViewModel
        if (switchId == 1) {
            KisanViewModel.updateValveStatus(valve1 = newState, valve2 = data.Valve2)
        } else {
            KisanViewModel.updateValveStatus(valve1 = data.Valve1, valve2 = newState)
        }

        // Update the overall irrigation status as well
        val anyValveOpen = (if (switchId == 1) newState else data.Valve1) || (if (switchId == 2) newState else data.Valve2)
        KisanViewModel.updateIrrigationStatus(anyValveOpen)

        // NOTE: The local 'fieldStates' will be updated by LaunchedEffect (FIX 1) when the RTDB confirms the write.
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- 1. Top Control Switches (Buttons) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Left Switch (ID 1)
            ControlSwitch(
                isActive = fieldStates.first { it.switchId == 1 }.isFlowing,
                onClick = { toggleFlow(1) },
                label = "Switch 1",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Right Switch (ID 2)
            ControlSwitch(
                isActive = fieldStates.first { it.switchId == 2 }.isFlowing,
                onClick = { toggleFlow(2) },
                label = "Switch 2",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // --- 2. The Field/Farm Layout with Pipes ---
        // FarmLayout now receives the synchronized state, removing the need for its own ViewModel logic.
        FarmLayout(fieldStates = fieldStates)
    }
}

@Composable
fun ControlSwitch(
    isActive: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else Color(0xFFC5C5C5)
        ),
        modifier = modifier
            .size(110.dp, 40.dp) // Size approximation for the image
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isActive) "STOP" else "START",
                fontWeight = FontWeight.Bold,
                color = if (isActive) MaterialTheme.colorScheme.onPrimary else Color.Black
            )
        }
    }
}

@Composable
fun FarmLayout(fieldStates: List<FieldState>) {
    // 0. Define Colors and Styles
    val pipeColor = MaterialTheme.colorScheme.primary
    val pipeStroke = 4.0F
    val fieldColor = Color(0xFF5CB85C)
    val containerColor = Color.LightGray

    // 🚨 REMOVED: val KisanViewModel: KisanViewModel = viewModel()
    // 🚨 REMOVED: val data by KisanViewModel.kisanData.collectAsState()

    // --- REFINED ANIMATION LOGIC (Now reads ONLY from the passed-in, synchronized fieldStates) ---
    // 1. Determine the flow state for Switch 1 (Fields 0 & 1)
    val isFlowing1 = fieldStates.first { it.switchId == 1 }.isFlowing // Reads from synchronized state
    // 2. Determine the flow state for Switch 2 (Fields 2 & 3)
    val isFlowing2 = fieldStates.first { it.switchId == 2 }.isFlowing // Reads from synchronized state

    // 3. Get the Animated Progress for Switch 1
    val progress1 by animateFloatAsState(
        targetValue = if (isFlowing1) 1f else 0f,
        animationSpec = if (isFlowing1) infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ) else tween(0),
        label = "FlowAnimation1"
    )

    // 4. Get the Animated Progress for Switch 2
    val progress2 by animateFloatAsState(
        targetValue = if (isFlowing2) 1f else 0f,
        animationSpec = if (isFlowing2) infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ) else tween(0),
        label = "FlowAnimation2"
    )
    // ---------------------------------

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalWidth = size.width
            val totalHeight = size.height
            val fieldWidth = totalWidth / 4.5f
            val fieldHeight = totalHeight * 0.9f
            val fieldCornerRadius = 16.dp.toPx()
            val spacing = (totalWidth - 4 * fieldWidth) / 5f

            drawRect(color = containerColor, size = size)

            for (i in 0..3) {
                val fieldLeft = spacing * (i + 1) + fieldWidth * i
                val fieldTop = totalHeight * 0.05f

                // --- A. Draw Field (Green Box) ---
                drawRoundRect(
                    color = fieldColor,
                    topLeft = Offset(fieldLeft, fieldTop),
                    size = Size(fieldWidth, fieldHeight),
                    cornerRadius = CornerRadius(fieldCornerRadius, fieldCornerRadius)
                )

                // --- B. Draw Pipe (Blue Line) ---
                val pipeCenter = fieldLeft + fieldWidth / 2f
                val pipeStart = 0f
                val pipeEnd = fieldTop + fieldHeight - 10.dp.toPx()

                // Decide which progress value to use based on the field index
                val progress = if (i < 2) progress1 else progress2
                val isFlowing = if (i < 2) isFlowing1 else isFlowing2

                // Draw the static blue pipe structure
                drawLine(
                    color = if (isFlowing) Accent else Color.Gray,
                    start = Offset(pipeCenter, pipeStart),
                    end = Offset(pipeCenter, pipeEnd+50),
                    strokeWidth = pipeStroke * 3f,
                    cap = StrokeCap.Round
                )

                // --- C. Draw Water Flow Animation ---
                if (isFlowing) {
                    // progress is a Float (0.0 to 1.0) and safe to use here.
                    val waterPosition = pipeStart + (pipeEnd - pipeStart) * progress
                    drawCircle(
                        color = Accent,
                        radius = pipeStroke * 8f,
                        center = Offset(pipeCenter, waterPosition)
                    )
                }

                // 🚨 REMOVED: All ViewModel update calls are removed from here to prevent infinite recomposition loops.
                // The database is only updated when the user clicks the switch button in FarmWaterControlScreen.
                /* if (isFlowing1 || isFlowing2) {
                    KisanViewModel.updateIrrigationStatus(true)
                    KisanViewModel.updateValveStatus(
                        valve1 = isFlowing1,
                        valve2 = isFlowing2
                    )
                } else {
                    KisanViewModel.updateIrrigationStatus(false)
                    KisanViewModel.updateValveStatus(
                        valve1 = isFlowing1,
                        valve2 = isFlowing2
                    )
                }
                */
            }
        }
    }
}