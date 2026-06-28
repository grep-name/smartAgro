package com.example.smartagro.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartagro.ui.screens.CropPage
import com.example.smartagro.ui.screens.HomePage
import com.example.smartagro.ui.screens.IrrigationPage
import com.example.smartagro.ui.screens.LoginPage
import com.example.smartagro.ui.screens.NodeData
import com.example.smartagro.ui.screens.PlaceNodePage
import com.example.smartagro.ui.screens.SelectCropPage
import com.example.smartagro.ui.screens.SplashScreen
import com.example.smartagro.ui.screens.WeatherPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginPage(navController)
        }
        composable("home") {
            HomePage(navController)
        }
        composable("weather") {
            WeatherPage(navController)
        }
        composable("crop") {
            CropPage(navController)
        }
        composable("select") {
            SelectCropPage(navController)
        }
        composable("irrigation") {
            IrrigationPage(navController)
        }
        composable(route = "place/{nodeId}") { backStackEntry ->
            PlaceNodePage(navController, backStackEntry.arguments?.getString("nodeId"))
        }
        composable(route = "node/{nodeId}") { backStackEntry ->
            NodeData(navController, backStackEntry.arguments?.getString("nodeId"))
        }
    }
}