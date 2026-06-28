package com.example.smartagro.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartagro.R
import com.example.smartagro.ui.theme.Accent
import com.example.smartagro.ui.theme.Primary
import com.example.smartagro.ui.theme.latoFontFamily
import com.example.smartagro.viewmodel.KisanViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectCropPage(navController: NavController) {
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

    var name by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("No location selected") }
    var latLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var loadingLocation by remember { mutableStateOf(false) }

    var cropType by remember { mutableStateOf("") }
    var farmType by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val kisanViewmodel: KisanViewModel = viewModel()

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // convenience lambda to request permission if not granted
    fun ensureLocationPermission() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // function to fetch location (best effort) and then reverse-geocode it
    fun fetchLocationAndAddress(onDone: (Pair<Double, Double>?) -> Unit) {
        ensureLocationPermission()
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            onDone(null)
            return
        }

        loadingLocation = true
        try {
            val fused = LocationServices.getFusedLocationProviderClient(context)
            fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onDone(Pair(location.latitude, location.longitude))
                    } else {
                        // fallback to lastLocation
                        fused.lastLocation.addOnSuccessListener { last ->
                            if (last != null) onDone(Pair(last.latitude, last.longitude))
                            else onDone(null)
                        }.addOnFailureListener {
                            onDone(null)
                        }
                    }
                }
                .addOnFailureListener {
                    loadingLocation = false
                    onDone(null)
                }
                .addOnCompleteListener {
                    loadingLocation = false
                }
        } catch (ex: Exception) {
            loadingLocation = false
            onDone(null)
        }
    }

    // Reverse geocode helper (runs in IO)
    fun reverseGeocodeAndSetText(lat: Double, lng: Double) {
        coroutineScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressString = withContext(Dispatchers.IO) {
                try {
                    val results = geocoder.getFromLocation(lat, lng, 1)
                    if (!results.isNullOrEmpty()) {
                        val addr = results[0]
                        // build a readable address from available lines
                        val lines = mutableListOf<String>()
                        for (i in 0..addr.maxAddressLineIndex) {
                            addr.getAddressLine(i)?.let { lines.add(it) }
                        }
                        // also append locality / admin / country if not already present
                        if (lines.isEmpty()) {
                            val fallbackParts = listOfNotNull(
                                addr.subLocality,
                                addr.locality,
                                addr.subAdminArea,
                                addr.adminArea,
                                addr.countryName
                            )
                            fallbackParts.joinToString(", ")
                        } else lines.joinToString(", ")
                    } else null
                } catch (e: Exception) {
                    null
                }
            }

            if (!addressString.isNullOrBlank()) {
                locationText = addressString
            } else {
                locationText = "Lat: %.6f, Lng: %.6f".format(lat, lng)
            }
        }
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopCenter)
                            .padding(
                                horizontal = 0.035 * screenWidth
                            )
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .windowInsetsPadding(
                                        WindowInsets.systemBars.only(
                                            WindowInsetsSides.Top
                                        )
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .clickable {
                                                navController.popBackStack()
                                            }
                                            .background(
                                                color = Accent.copy(alpha = 0.12f),
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
                                            tint = Primary,
                                            modifier = Modifier
                                                .size(30.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(0.03 * screenWidth))
                                    Text(
                                        text = "Select Your Crop",
                                        color = Primary,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.size(0.03 * screenHeight))
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                },
                                placeholder = {
                                    Text(
                                        "Enter Your Name",
                                        color = Accent,
                                        fontWeight = FontWeight.W500,
                                        fontSize = 20.sp
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(size = 16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Primary,
                                    focusedLabelColor = Primary,
                                    cursorColor = Primary,
                                    focusedTextColor = Accent,
                                    unfocusedTextColor = Accent,
                                ),
                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    color = Accent,
                                    fontWeight = FontWeight.W500
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                    }
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.size(0.02 * screenHeight))
                            Text(
                                text = "Select a Crop",
                                color = Accent,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = latoFontFamily,
                            )
                            Spacer(modifier = Modifier.size(0.015 * screenHeight))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (cropType == "Maize") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                cropType = "Maize"
                                            }
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.maize),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Maize",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                                Spacer(modifier = Modifier.size(0.015 * screenWidth))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (cropType == "Potato") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                cropType = "Potato"
                                            }
                                        ),
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.potato),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Potatoes",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(0.015 * screenHeight))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (cropType == "Orange") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                cropType = "Orange"
                                            }
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.orange),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Oranges",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                                Spacer(modifier = Modifier.size(0.015 * screenWidth))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (cropType == "Cardamom") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                cropType = "Cardamom"
                                            }
                                        ),
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.cardamom),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Cardamom",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.size(0.02 * screenHeight))
                            Text(
                                text = "Select Farm Type",
                                color = Accent,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = latoFontFamily,
                            )
                            Spacer(modifier = Modifier.size(0.015 * screenHeight))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (farmType == "Terrace") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                farmType = "Terrace"
                                            }
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.terrace),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Terrace",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                                Spacer(modifier = Modifier.size(0.015 * screenWidth))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = 3.dp,
                                            color = if (farmType == "Flat Land") Primary else Color(
                                                0xFFDADADA
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(0.25 * screenHeight)
                                        .clickable(
                                            onClick = {
                                                farmType = "Flat Land"
                                            }
                                        ),
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.farming),
                                        contentDescription = "Select Crop Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .fillMaxWidth()
                                            .height(0.21 * screenHeight)
                                    )
                                    Text(
                                        text = "Flat Land",
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }

                        // NEW: Location controls and Map + reverse geocoding
                        item {
                            Spacer(modifier = Modifier.size(0.02 * screenHeight))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // request permission first and then fetch location & address
                                        ensureLocationPermission()
                                        coroutineScope.launch {
                                            fetchLocationAndAddress { pair ->
                                                if (pair != null) {
                                                    latLng = pair
                                                    // immediately convert to address and set locationText
                                                    reverseGeocodeAndSetText(pair.first, pair.second)
                                                } else {
                                                    locationText = "Location unavailable"
                                                }
                                            }
                                        }
                                    },
                                ) {
                                    Text(
                                        text = "Get location",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Accent
                                    )
                                }
                                Spacer(modifier = Modifier.size(0.03 * screenWidth))
                                if (loadingLocation) {
                                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
                                } else {
                                    Text(
                                        text = locationText,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(0.02 * screenHeight))

                            // Show the map only if we have a location AND farm type is selected
                            if (latLng != null) {
                                val cameraPositionState: CameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(
                                        LatLng(latLng!!.first, latLng!!.second),
                                        15f
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.35 * screenHeight)
                                        .border(
                                            width = 3.dp,
                                            color = Primary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GoogleMap(
                                        modifier = Modifier.fillMaxSize(),
                                        cameraPositionState = cameraPositionState
                                    ) {
                                        Marker(
                                            state = MarkerState(position = LatLng(latLng!!.first, latLng!!.second)),
                                            title = "Your farm location",
                                            snippet = farmType
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Tap Get location to show map and address.",
                                    fontSize = 14.sp,
                                    color = Accent
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.size(0.02 * screenHeight))
                            FloatingActionButton(
                                onClick = {
                                    if (locationText == "No location selected" || latLng == null) {
                                        Toast.makeText(
                                            context,
                                            "Please select your location",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@FloatingActionButton
                                    }else{
                                        kisanViewmodel.updateKisanMetadata(
                                            newName = name,
                                            newCrop = cropType,
                                            newFarmType = farmType,
                                            newLatitude = latLng!!.first,
                                            newLongitude = latLng!!.second,
                                            newLocation = locationText,
                                            parent = true
                                        )
                                        navController.navigate("home")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                containerColor = Primary,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp,
                                    focusedElevation = 0.dp,
                                    hoveredElevation = 0.dp
                                )
                            ) {
                                Text(
                                    text = "Submit Setup",
                                    fontFamily = latoFontFamily,
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Spacer(modifier = Modifier.size(0.03 * screenHeight))
                        }
                    }
                }
            }
        }
    )
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Preview(showSystemUi = true)
@Composable
fun SelectCropPagePreview() {
    SelectCropPage(rememberNavController())
}
