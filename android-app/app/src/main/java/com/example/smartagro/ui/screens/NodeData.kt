package com.example.smartagro.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartagro.ui.components.AnimatedStatusIndicator
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Background
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NodeData(navController: NavController, nodeId: String? = "Node1") {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, view) }

    if (windowInsetsController != null) {
        windowInsetsController.isAppearanceLightStatusBars = true
    }
    val KisanViewModel: KisanViewModel = viewModel()
    val data by KisanViewModel.kisanData.collectAsState()

    var soiltemp = 0.0
    var soilmoisture = 0.0

    when (nodeId) {
        "Node1" -> {
            soiltemp = data.FarmData.Node1.SoilTemperature
            soilmoisture = data.FarmData.Node1.SoilMoisture
        }
        "Node2" -> {
            soiltemp = data.FarmData.Node2.SoilTemperature
            soilmoisture = data.FarmData.Node2.SoilMoisture
        }
        "Node3" -> {
            soiltemp = data.FarmData.Node3.SoilTemperature
            soilmoisture = data.FarmData.Node3.SoilMoisture
        }
        "Node4" -> {
            soiltemp = data.FarmData.Node4.SoilTemperature
            soilmoisture = data.FarmData.Node4.SoilMoisture
        }
    }

    var okTemp by remember { mutableStateOf(true) }
    var okMoist by remember { mutableStateOf(true) }

    okMoist = soilmoisture in 35.0..70.0
    okTemp = soiltemp in 18.0..26.0

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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopCenter)
                    ) {
                        item {
                            Box() {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Primary,
                                            shape = RoundedCornerShape(
                                                bottomEnd = 35.dp,
                                                bottomStart = 35.dp
                                            )
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .windowInsetsPadding(
                                                WindowInsets.systemBars.only(
                                                    WindowInsetsSides.Top
                                                )
                                            )
                                            .padding(
                                                horizontal = 0.05 * screenWidth
                                            )
                                    ) {
                                        Spacer(modifier = Modifier.size(0.02 * screenHeight))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clickable {
                                                        navController.popBackStack()
                                                    }
                                                    .align(Alignment.CenterVertically)
                                                    .background(
                                                        color = Color(0xFFFFFFFF).copy(alpha = 0.12f),
                                                        shape = RoundedCornerShape(50.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = Color.White.copy(alpha = 0.3f),
                                                        shape = RoundedCornerShape(50.dp)
                                                    )
                                                    .padding(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "settings_icon",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.size(0.03 * screenWidth))
                                            Text(
                                                text = "Your Crops",
                                                color = Color.White,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                        Column(
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("weather")
                                                }
                                                .fillMaxWidth()
                                                .background(
                                                    color = Color(0x0FFFFFFF).copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.White.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .padding(0.04 * screenWidth),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Crop Status",
                                                color = Color.White,
                                                fontSize = 32.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                            )
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                AnimatedStatusIndicator(
                                                    isTrue = if (okTemp && okMoist){true} else {false},
                                                    modifier = Modifier.size(0.1 * screenHeight)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.size(0.005 * screenHeight))
                            Box(
                                modifier = Modifier
                                    .clickable {
                                    }
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 0.04 * screenWidth,
                                        vertical = 0.005 * screenHeight
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Color(0xFFFFFFFF),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(0.04 * screenWidth)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Soil Temperature",
                                            color = Color.Black,
                                            fontSize = 26.sp,
                                            fontFamily = latoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(0.015 * screenHeight))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        // --- COLUMN 1: WATER TANK ---
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            AnimatedStatusIndicator(
                                                isTrue = if (soiltemp>26){false} else if(soiltemp<18){false} else {true},
                                                modifier = Modifier.size(0.1 * screenHeight)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(0.01 * screenWidth))
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            Text(
                                                text = "Temperature",
                                                fontSize = 28.sp,
                                                fontFamily = latoFontFamily,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "$soiltemp°C",
                                                fontSize = 52.sp,
                                                fontFamily = latoFontFamily,
                                                color = Accent,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = if (soiltemp >= 26){"Too High"}else if(soiltemp <=18){"Too Low"} else "Normal",
                                                color = if (soiltemp >= 26){Color(0xFFFFC107)
                                                }else if(soiltemp <=18){Color(0xFFFFCE3A)
                                                } else Primary,
                                                fontSize = 24.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(0.03 * screenWidth))
                                    Text(
                                        text = "No Recommendations or Alerts",
                                        color = Color.Black,
                                        fontSize = 22.sp,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.size(0.005 * screenHeight))
                            Box(
                                modifier = Modifier
                                    .clickable {
                                    }
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 0.04 * screenWidth,
                                        vertical = 0.005 * screenHeight
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Color(0xFFFFFFFF),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(0.04 * screenWidth)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Soil Moisture",
                                            color = Color.Black,
                                            fontSize = 26.sp,
                                            fontFamily = latoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(0.015 * screenHeight))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        // --- COLUMN 1: WATER TANK ---
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            AnimatedStatusIndicator(
                                                isTrue = if (soilmoisture>=70){false} else if(soilmoisture<=35){false} else {true},
                                                modifier = Modifier.size(0.1 * screenHeight)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(0.01 * screenWidth))
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            Text(
                                                text = "Moisture",
                                                fontSize = 28.sp,
                                                fontFamily = latoFontFamily,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "$soilmoisture%",
                                                fontSize = 52.sp,
                                                fontFamily = latoFontFamily,
                                                color = Accent,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = if (soilmoisture >= 70){"Too High"}else if(soilmoisture <=35){"Too Low"} else "Normal",
                                                color = if (soilmoisture >= 70){Color(0xFFFFC107)
                                                }else if(soilmoisture <=35){Color(0xFFFFCE3A)
                                                } else Primary,
                                                fontSize = 24.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(0.03 * screenWidth))
                                    Text(
                                        text = "Recommendation: Stop next irrigation round to lower soil moisture levels.",
                                        color = Color(0xFFD5AB2D),
                                        fontSize = 22.sp,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(0.04 * screenHeight))
                        }
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun NodeDataPagePreview(){
    NodeData(rememberNavController())
}