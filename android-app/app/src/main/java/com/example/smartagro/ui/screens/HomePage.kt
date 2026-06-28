package com.example.smartagro.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
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
import com.example.smartagro.R
import com.example.smartagro.ui.components.LastIrrigatedBadge
import com.example.smartagro.ui.components.ProbeCapsule
import com.example.smartagro.ui.components.ProbeData
import com.example.smartagro.ui.components.WaterTank
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Background
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.Secondary
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class WeatherData(
    val humidity: Int = 0,
    val temperature: Int = 0,
)

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateOnly(instant: Instant): String {
    val istZoneId = ZoneId.of("Asia/Kolkata")
    val zonedDateTime = instant.atZone(istZoneId)

    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.US)

    return dateFormatter.format(zonedDateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, view) }

    if (windowInsetsController != null) {
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    val sampleIrrigationTime = Instant.now().minus(0, ChronoUnit.HOURS)
    val date = formatDateOnly(sampleIrrigationTime)

    val KisanViewModel: KisanViewModel = viewModel()
    val data by KisanViewModel.kisanData.collectAsState()

    val humidity = data.FarmData.Node1.Humidity
    val temperature = data.FarmData.Node1.Temperature

    val probes: List<ProbeData> = listOf(
        ProbeData(1, (100 - (data.FarmData.Node1.SoilMoisture.toFloat())) * 0.01f, data.FarmData.Node1.SoilTemperature.toInt()),
        ProbeData(2, data.FarmData.Node2.SoilMoisture.toFloat() * 0.01f, data.FarmData.Node2.SoilTemperature.toInt()),
        ProbeData(3, data.FarmData.Node3.SoilMoisture.toFloat() * 0.01f, data.FarmData.Node3.SoilTemperature.toInt()),
        ProbeData(4, data.FarmData.Node4.SoilMoisture.toFloat() * 0.01f, data.FarmData.Node4.SoilTemperature.toInt())
    )

    val kisanname = data.Name
    val location = data.Location

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
                    ){
                        item{
                            Box(){
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
                                    ){
                                        Spacer(modifier = Modifier.size(0.02 * screenHeight))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Namaste, $kisanname",
                                                    color = Color.White,
                                                    fontSize = 32.sp,
                                                    fontFamily = latoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                )
                                                Text(
                                                    text = location,
                                                    color = Secondary,
                                                    fontSize = 20.sp,
                                                    fontFamily = latoFontFamily,
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                        Column (
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("weather")
                                                }
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
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(R.drawable.sunny),
                                                    contentDescription = "weather_icon",
                                                    modifier = Modifier
                                                        .size(0.1 * screenHeight)                                                )
                                                Spacer(modifier = Modifier.size(0.03 * screenWidth))
                                                Column(
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = "Weather Station",
                                                        color = Secondary,
                                                        fontSize = 22.sp,
                                                        fontFamily = latoFontFamily,
                                                        modifier = Modifier
                                                    )
                                                    Text(
                                                        text = "Clear Skies",
                                                        color = Color.White,
                                                        fontSize = 32.sp,
                                                        fontFamily = latoFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier
                                                    )
                                                }
                                            }
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                Text(
                                                    text = "$temperature°C",
                                                    color = Color.White,
                                                    fontSize = 68.sp,
                                                    fontFamily = latoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                )
                                                Text(
                                                    text = "Tap for details",
                                                    color = Secondary,
                                                    fontSize = 20.sp,
                                                    fontFamily = latoFontFamily,
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                    }
                                }
                            }
                        }
                        item{
                            Spacer(modifier = Modifier.size(0.005 * screenHeight))
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("irrigation")
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
                                            text = "Irrigation Status",
                                            color = Color.Black,
                                            fontSize = 26.sp,
                                            fontFamily = latoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                        )
                                        Text(
                                            text = if (data.IrrigationStatus) "System Active" else "System Idle",
                                            color = Accent,
                                            fontSize = 18.sp,
                                            fontFamily = latoFontFamily,
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(0.015 * screenHeight))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ){
                                        // --- COLUMN 1: WATER TANK ---
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            WaterTank(
                                                percentage = data.WaterTank.toFloat() * 0.01f,
                                                modifier = Modifier
                                                    .width(165.dp)
                                                    .height(180.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(0.01 * screenWidth))
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ){
                                            Text(
                                                text = "Water Level",
                                                fontSize = 28.sp,
                                                fontFamily = latoFontFamily,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${data.WaterTank}%",
                                                fontSize = 62.sp,
                                                fontFamily = latoFontFamily,
                                                color = Accent,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Trench Grp1",
                                                color = Color(0xFFA1A7B0),
                                                fontSize = 20.sp,
                                                fontFamily = latoFontFamily,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(0.01 * screenHeight))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            Text(
                                                text = "Last Irrigated",
                                                fontSize = 28.sp,
                                                fontFamily = latoFontFamily,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = data.LastIrrigated,
                                                fontSize = 46.sp,
                                                fontFamily = latoFontFamily,
                                                color = Primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                        ) {
                                            LastIrrigatedBadge(
                                                timestamp = data.LastTime,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("crop")
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
                                            text = "Soil Data from Probes",
                                            color = Color.Black,
                                            fontSize = 22.sp,
                                            fontFamily = latoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(0.015 * screenHeight))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp) // Gap between probes
                                    ) {
                                        probes.forEach { probe ->
                                            ProbeCapsule(
                                                data = probe,
                                                modifier = Modifier.weight(1f) // Equal width for all 4
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(0.01 * screenHeight))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Probe 1",
                                            color = Primary,
                                            fontSize = 18.sp,
                                            fontFamily = latoFontFamily,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "Probe 2",
                                            color = Primary,
                                            fontSize = 18.sp,
                                            fontFamily = latoFontFamily,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "Probe 3",
                                            color = Primary,
                                            fontSize = 18.sp,
                                            fontFamily = latoFontFamily,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "Probe 4",
                                            color = Primary,
                                            fontSize = 18.sp,
                                            fontFamily = latoFontFamily,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.size(0.035 * screenHeight))
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun HomePagePreview(){
    HomePage(rememberNavController())
}