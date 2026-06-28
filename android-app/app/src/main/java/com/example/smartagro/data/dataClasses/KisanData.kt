package com.example.smartagro.data.dataClasses

data class WeatherData(
    // NOTE: Using Double for better compatibility with potential float values,
    // even if the image shows them as integers.
    val Humidity: Double = 28.0,
    val Temperature: Double = 24.0,
    val RainPercent: Double = 0.0,
    val WindSpeed: Double = 3.0
)

data class Node1Data( // Represents the sensor data inside 'Node1'
    val Temperature: Double = 23.4,
    val SoilMoisture: Double = 68.56,
    val SoilTemperature: Double = 18.6,
    val Humidity: Double = 30.2,
)

data class Node2Data( // Represents the sensor data inside 'Node1'
    val Temperature: Double = 21.4,
    val SoilMoisture: Double = 61.26,
    val SoilTemperature: Double = 16.97,
    val Humidity: Double = 31.2,
)

data class Node3Data( // Represents the sensor data inside 'Node1'
    val Temperature: Double =20.98,
    val SoilMoisture: Double = 55.13,
    val SoilTemperature: Double = 19.0,
    val Humidity: Double = 29.35,
)

data class Node4Data( // Represents the sensor data inside 'Node1'
    val Temperature: Double = 23.4,
    val SoilMoisture: Double = 57.22,
    val SoilTemperature: Double = 19.6,
    val Humidity: Double = 35.2,
)

data class FarmDataContainer( // Represents the parent of Node1 (i.e., the original 'FarmData' node)
    val Node1: Node1Data = Node1Data(),
    val Node2: Node2Data = Node2Data(),
    val Node3: Node3Data = Node3Data(),
    val Node4: Node4Data = Node4Data(),
    // You can add Node2, Node3, etc., here if needed: val Node2: Node1Data = Node1Data()
)

data class KisanData(
    val FarmSizeAcres: Double = 1.5,
    val Latitude: Double = 26.8402332, // Matches the key in your image
    val Longitude: Double = 75.5620009, // New field for sending data
    val Location: String = "RHR6+4P3, Dahmi Kalan, Rajasthan 303007, India", // Matches the key in your image
    val Crop: String = "Maize",
    val FarmType: String = "Flat Land",
    val Name: String = "Amogh",
    val IrrigationStatus: Boolean = false,
    val Valve1: Boolean = false,
    val Valve2: Boolean = false,
    val Node1: Int = 0,
    val Node2: Int = 0,
    val Node3: Int = 0,
    val Node4: Int = 0,
    val LastTime: String = "10:35 am",
    val LastIrrigated: String = "Dec 12",
    val WaterTank: Int = 72,
    val Parent: Boolean = true,

    // Nested structures
    val FarmData: FarmDataContainer = FarmDataContainer(), // Points to the Node1 holder
    val WeatherData: WeatherData = WeatherData(),
    val IrrigationData: Any? = null, // Kept this as you had it
)