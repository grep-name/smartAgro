package com.example.smartagro.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.Secondary
import com.example.smartagro.ui.theme.Surface
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel
import com.google.zxing.integration.android.IntentIntegrator


// Data class to store the result
data class ScanResultState(
    val scannedText: String? = null,
    val isScanning: Boolean = false
)

// Dummy function to simulate storing data (e.g., to a database or local storage)
fun storeScannedText(text: String?) {
    if (text != null) {
        // Replace this with your actual ViewModel/Database write logic
        println("--- SAVING DATA ---")
        println("Scanned and Stored: $text")
        // Example: myViewModel.saveQrCode(text)
        // ... (Your actual data storage logic goes here) ...
        println("--- SAVE COMPLETE ---")
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginPage(navController: NavController) {
    // Implementation of the Login Page UI goes here
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var mobileNumber by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, view) }

    if (windowInsetsController != null) {
        windowInsetsController.isAppearanceLightStatusBars = true
    }
    val context = LocalContext.current

    // State to hold the result and UI status
    var scanState by remember { mutableStateOf(ScanResultState()) }

    val KisanViewModel: KisanViewModel = viewModel()
    val data by KisanViewModel.kisanData.collectAsState()

    // --- 1. Activity Result Launcher for ZXing ---
    val scanLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(
            IntentIntegrator.REQUEST_CODE,
            result.resultCode,
            result.data
        )

        val scannedText = intentResult?.contents

        scanState = ScanResultState(
            scannedText = scannedText,
            isScanning = false
        )

        storeScannedText(scannedText)

        if (scannedText?.contains("Node1") ?: false && data.Node1!=0) {
            navController.navigate("node/$scannedText")
        } else if (scannedText?.contains("Node2") ?: false && data.Node2!=0) {
            navController.navigate("node/$scannedText")
        }else if (scannedText?.contains("Node3") ?: false && data.Node3!=0) {
            navController.navigate("node/$scannedText")
        }else if (scannedText?.contains("Node4") ?: false && data.Node4!=0) {
            navController.navigate("node/$scannedText")
        }else if (scannedText?.contains("Node") ?: false) {
            navController.navigate("place/$scannedText")
        }else if (scannedText?.contains("Parent") ?: false && data.Parent) {
            navController.navigate("home")
        }else if (scannedText?.contains("Parent") ?: false) {
            navController.navigate("select")
        }
    }

    // --- 3. Function to Launch the Scanner ---
    // inside your composable where scanLauncher is defined
    fun startScan() {
        val integrator = IntentIntegrator(context as ComponentActivity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(true)

        val intent = integrator.createScanIntent()  // <- createScanIntent() returns the configured Intent
        scanLauncher.launch(intent)

        scanState = ScanResultState(isScanning = true)
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                        .padding(
                            horizontal = 0.035 * screenWidth
                        )
                ) {
//                    Spacer(modifier = Modifier.size(0.5 * screenHeight))
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Login Background",
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20))
                                .size(0.3 * screenWidth)
                        )
                        Spacer(modifier = Modifier.size(0.01 * screenHeight))
                        Text(
                            text = "Welcome to",
                            color = Primary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = latoFontFamily,
                        )
                        Text(
                            text = "SmartAgro",
                            color = Primary,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
                        )
                        Spacer(modifier = Modifier.size(0.02 * screenHeight))
                        Text(
                            text = "Your Smart Agriculture Companion",
                            color = Accent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
                        )
                        Spacer(modifier = Modifier.size(0.12 * screenHeight))
                        if (scanState.isScanning) {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("Camera is active... point it at a QR code.")
                        } else {
                            Button(onClick = ::startScan) {
                                Text("Start QR Code Scan")
                            }
                            Spacer(Modifier.height(32.dp))

                            if (scanState.scannedText != null) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Scan Successful!",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        // Display the fetched text
                                        Text(
                                            text = "Fetched Text: ${scanState.scannedText}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Text("Text has been stored.")
                                    }
                                }
                            } else if (!scanState.isScanning) {
                                // Initial state or scan failed/cancelled
                                Text(
                                    text = "Tap the button to scan a node and continue to your account.",
                                    color = Accent,
                                    fontFamily = latoFontFamily,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
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
fun LoginPagePreview() {
    LoginPage(rememberNavController())
}