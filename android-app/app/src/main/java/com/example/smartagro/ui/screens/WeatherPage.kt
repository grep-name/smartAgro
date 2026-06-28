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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.smartagro.ui.components.ForecastItem
import com.example.smartagro.ui.components.ForecastRow
import com.example.smartagro.ui.components.WeatherCard
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Background
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.Secondary
import com.example.smartagro.ui.theme.SproutGreen
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel

data class WeatherItem(
    val label: String,
    val value: String,
    val icon: Painter,
    val color: Color
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherPage(navController: NavController) {
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

    // 2. Safely access the nested data
    val humidity = data.FarmData.Node1.Humidity
    val temperature = data.FarmData.Node1.Temperature
    val rain = data.WeatherData.RainPercent

    val weatherData = listOf(
        WeatherItem(
            label = "HUMIDITY",
            value = "$humidity%",
            icon = painterResource(R.drawable.waterdrop),
            color = Color(0xFF42A5F5) // Blue
        ),
        WeatherItem(
            label = "RAIN CHANCE",
            value = "$rain%",
            icon = painterResource(R.drawable.cloudy), // Simulating rain/cloud
            color = Color(0xFF7E57C2) // Purple
        )
    )
    val forecastData = listOf(
        ForecastItem("6:40pm", "Rainy", painterResource(R.drawable.sunny), Color.Unspecified, 10),
        ForecastItem("7:40pm", "Haze", painterResource(R.drawable.sunny), Color.Unspecified,14),
        ForecastItem("8:40pm", "Haze", painterResource(R.drawable.cloudy), Color.Unspecified, 57)
    )

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
                                                text = "Your Weather",
                                                color = Color.White,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.size(0.03 * screenHeight))
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = data.Location,
                                                color = Secondary,
                                                fontSize = 20.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.size(0.01 * screenHeight))
                                            Text(
                                                text = "${temperature}°C",
                                                color = Color.White,
                                                fontSize = 80.sp,
                                                fontFamily = latoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                            )
                                            Spacer(modifier = Modifier.size(0.01 * screenHeight))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.sunny),
                                                    contentDescription = "weather_icon",
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                )
                                                Spacer(modifier = Modifier.size(0.02 * screenWidth))
                                                Text(
                                                    text = "Rain Not Expected",
                                                    color = Color.White,
                                                    fontSize = 22.sp,
                                                    fontFamily = latoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                )
                                            }
                                            Spacer(modifier = Modifier.size(0.035 * screenHeight))
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                weatherData.chunked(2).forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        rowItems.forEach { item ->
                                            Box(modifier = Modifier.weight(1f)) {
                                                WeatherCard(item)
                                            }
                                        }

                                        if (rowItems.size < 2) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(24.dp),
                                        spotColor = Color.LightGray.copy(alpha = 0.4f)
                                    ),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    // Header Row: Icon + Title
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DateRange,
                                            contentDescription = "Forecast",
                                            tint = Color(0xFF4285F4), // Google Blue-ish
                                            modifier = Modifier.size(30.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Forecast",
                                            fontSize = 24.sp,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2d3436)
                                        )
                                    }

                                    // List Items
                                    forecastData.forEachIndexed { index, item ->
                                        ForecastRow(item)

                                        // Add spacer between items, but not after the last one
                                        if (index < forecastData.size - 1) {
                                            Spacer(modifier = Modifier.height(24.dp))
                                        }
                                    }
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
fun WeatherPagePreview(){
    WeatherPage(rememberNavController())
}