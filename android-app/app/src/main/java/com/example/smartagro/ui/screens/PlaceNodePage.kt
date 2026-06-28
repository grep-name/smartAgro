package com.example.smartagro.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartagro.ui.components.PulsingStatusIndicator
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Background
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel

@Composable
fun PlaceNodePage(navController: NavController, nodeId: String?) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, view) }

    if (windowInsetsController != null) {
        windowInsetsController.isAppearanceLightStatusBars = true
    }
    var selected by remember { mutableStateOf(false) }
    var nodePos by remember { mutableStateOf(0) }
    val showDialog = selected
    var showDialog2 by remember { mutableStateOf(false) }

    val KisanViewModel: KisanViewModel = viewModel()
    val data by KisanViewModel.kisanData.collectAsState()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                selected = false
                nodePos = 0},
            title = { Text("Confirm Node Placement") },
            text = { Text("Place the node on Pipe. Scan the parent if all nodes placed, else continue...") },
            confirmButton = {
                Button(onClick = {
                    KisanViewModel.updateNodePosition(nodeId ?: "", nodePos)
                    showDialog2 = true
                    navController.navigate("login")
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    selected = false
                    nodePos = 0
                }) {
                    Text("CANCEL")
                }
            }
        )
    }
    if (showDialog2) {
        AlertDialog(
            onDismissRequest = {
                showDialog2 = false},
            title = { Text("Scan next QR ") },
            text = { Text("Scan the next qr code or the parent one to continue") },
            confirmButton = {
                Button(onClick = {
                    KisanViewModel.updateNodePosition(nodeId ?: "", nodePos)
                    showDialog2 = true
                    navController.navigate("login")
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    selected = false
                    nodePos = 0
                }) {
                    Text("CANCEL")
                }
            }
        )
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .windowInsetsPadding(
                                WindowInsets.systemBars.only(
                                    WindowInsetsSides.Top
                                )
                            )
                            .fillMaxSize()
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Place Node in Field",
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF5CB85C))
                                .height(0.4 * screenHeight)
                        ){
                            Row(
                                modifier = Modifier
                                    .height(0.4 * screenHeight)
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(0.38 * screenHeight)
                                        .width(10.dp)
                                        .background(Accent)
                                ){}
                                Box(
                                    modifier = Modifier
                                        .height(0.38 * screenHeight)
                                        .width(10.dp)
                                        .background(Accent)
                                ){}
                                Box(
                                    modifier = Modifier
                                        .height(0.38 * screenHeight)
                                        .width(10.dp)
                                        .background(Accent)
                                ){}
                                Box(
                                    modifier = Modifier
                                        .height(0.38 * screenHeight)
                                        .width(10.dp)
                                        .background(Accent)
                                ){}
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 1 || data.Node2 == 1 || data.Node3 == 1 || data.Node4 == 1) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 1 || data.Node2 == 1 || data.Node3 == 1 || data.Node4 == 1) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 1
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 2 || data.Node2 == 2 || data.Node3 == 2 || data.Node4 == 2) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 2 || data.Node2 == 2 || data.Node3 == 2 || data.Node4 == 2) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 2
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 3 || data.Node2 == 3 || data.Node3 == 3 || data.Node4 == 3) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 3 || data.Node2 == 3 || data.Node3 == 3 || data.Node4 == 3) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 3
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 4 || data.Node2 == 4 || data.Node3 == 4 || data.Node4 == 4) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 4 || data.Node2 == 4 || data.Node3 == 4 || data.Node4 == 4) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 4
                                                    }
                                                }
                                            )
                                    ){}
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 5 || data.Node2 == 5 || data.Node3 == 5 || data.Node4 == 5) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 5 || data.Node2 == 5 || data.Node3 == 5 || data.Node4 == 5) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 5
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 6 || data.Node2 == 6 || data.Node3 == 6 || data.Node4 == 6) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 6 || data.Node2 == 6 || data.Node3 == 6 || data.Node4 == 6) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 6
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 7 || data.Node2 == 7 || data.Node3 == 7 || data.Node4 == 7) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 7 || data.Node2 == 7 || data.Node3 == 7 || data.Node4 == 7) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 7
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 8 || data.Node2 == 8 || data.Node3 == 8 || data.Node4 == 8) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 8 || data.Node2 == 8 || data.Node3 == 8 || data.Node4 == 8) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 8
                                                    }
                                                }
                                            )
                                    ){}
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 9 || data.Node2 == 9 || data.Node3 == 9 || data.Node4 == 9) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 9 || data.Node2 == 9 || data.Node3 == 9 || data.Node4 == 9) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 9
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 10 || data.Node2 == 10 || data.Node3 == 10 || data.Node4 == 10) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 10 || data.Node2 == 10 || data.Node3 == 10 || data.Node4 == 10) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 10
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 11 || data.Node2 == 11 || data.Node3 == 11 || data.Node4 == 11) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 11 || data.Node2 == 11 || data.Node3 == 11 || data.Node4 == 11) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 11
                                                    }
                                                }
                                            )
                                    ){}
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                if (data.Node1 == 12 || data.Node2 == 12 || data.Node3 == 12 || data.Node4 == 12) Color(
                                                    0xFFFF9800
                                                ) else Color(0xFF484747)
                                            )
                                            .clickable(
                                                onClick = {
                                                    if (data.Node1 == 12 || data.Node2 == 12 || data.Node3 == 12 || data.Node4 == 12) {
                                                        Toast.makeText(
                                                            view.context,
                                                            "Position already occupied!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else{
                                                        selected = true
                                                        nodePos = 12
                                                    }
                                                }
                                            )
                                    ){}
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Tap to select a position to place the node from above grid.",
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        PulsingStatusIndicator(
                            size = 120.dp,
                            pulseColor = Color(0xFF4CAF50), // Green color
                            durationMillis = 600 // A slower, more gentle pulse
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if (nodeId != null) {
                            Text(
                                text = nodeId,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = Accent,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    )
}

