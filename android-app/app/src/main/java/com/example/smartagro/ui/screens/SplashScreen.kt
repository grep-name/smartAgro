package com.example.smartagro.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale // <-- New Import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.smartagro.R
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.latoFontFamily
import kotlinx.coroutines.delay // <-- New Import

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(navController: NavController) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // --- 1. Animation State ---
    // Start scale at 1.2 (slightly zoomed in)
    var startAnimation by remember { mutableStateOf(false) }

    // Animate the scale from 1.2 down to 1.0 over 2000ms (2 seconds)
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.0f else 1.2f,
        animationSpec = tween(
            durationMillis = 1000 // 1 seconds
        ),
        label = "ZoomAnimation"
    )

    // --- 2. Side Effect for Timing and Navigation ---
    LaunchedEffect(key1 = true) {
        // Start the zoom-out animation immediately
        startAnimation = true

        // Wait for the animation duration (2000ms)
        delay(1000)

        // Wait for the additional half second delay
        delay(500)

        // Navigate to the next screen ("home")
        navController.popBackStack() // Clear the back stack
        navController.navigate("login") {
            // Ensure the splash screen is not on the back stack
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    // Standard system bar setup (kept from your original code)
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, view) }
    if (windowInsetsController != null) {
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf4f6eb))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter =  painterResource(id = R.drawable.farmerrr),
                            contentDescription = "Splash Screen",
                            // --- 3. Apply the animated scale modifier ---
                            modifier = Modifier
                                .height(0.55 * screenHeight)
                                .scale(scale), // <-- Applied here!
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Welcome to SmartAgro",
                            modifier = Modifier
                                .padding(top = 32.dp),
                            color = Color.Black,
                            fontSize = 36.sp,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                            .align(Alignment.BottomCenter)
                            .padding(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally // Center the indicator
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(0.15 * screenHeight),
                            color = Primary
                        )
                    }
                }
            }
        }
    )
}