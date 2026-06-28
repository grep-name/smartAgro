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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
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
import com.example.smartagro.ui.components.FarmWaterControlScreen
import com.example.smartagro.ui.components.IrrigationIndicator
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Background
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IrrigationPage(navController: NavController) {
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
                            Box {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Accent,
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
                                                text = "Irrigation Page",
                                                color = Color.White,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 30.sp
                                            )

                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                        Column (
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = Color(0x0FFFFFFF).copy(alpha = 0.12f),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.White.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .padding(0.04 * screenWidth),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Text(
                                                text = "Irrigation Status",
                                                color = Color.White,
                                                fontSize = 32.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                            )
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                IrrigationIndicator(
                                                    isTrue = data.IrrigationStatus,
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(0.025 * screenHeight))
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.size(0.025 * screenHeight))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Farm Water Control",
                                    fontFamily = latoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp,
                                    color = Primary
                                )
                                FarmWaterControlScreen()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 0.06 * screenWidth
                                        ),
                                ) {
                                    Text(
                                        text = "Valve 1",
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 26.sp,
                                        color = Primary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "Valve 2",
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 26.sp,
                                        color = Primary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun IrrigationPagePreview() {
    IrrigationPage(rememberNavController())
}