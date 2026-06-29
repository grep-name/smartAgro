# 🌱 SmartAgro Ecosystem

> **An AI-powered IoT ecosystem for precision agriculture in hilly terrains**
---

# 📖 Overview

Traditional precision agriculture systems assume flat farmland and uniform sensor deployment. These assumptions become inefficient in mountainous regions where:

* Terrain creates communication shadow zones
* Water sources are scarce despite high humidity
* Weather varies significantly within small geographical regions
* Conventional sensor placement results in redundant hardware and higher costs

SmartAgro addresses these challenges through:

* Optimized sensor deployment algorithms
* ESP32 mesh networking
* Hyper-local weather prediction
* AI-assisted irrigation scheduling
* Fog water harvesting
* Real-time farmer monitoring applications

---

# 🚀 Features

## 🌾 Smart Soil Monitoring

* Soil moisture monitoring
* Soil temperature monitoring
* Ambient temperature monitoring
* Atmospheric pressure monitoring
* Continuous micro-climate analysis

---

## 📡 Optimized IoT Network

* ESP32-based sensor nodes
* Mesh networking
* Hexagonal tessellation deployment
* Terrace-aware linear node placement
* Hardware fault detection
* Low-power operation

---

## 🤖 AI Weather Prediction

* Hybrid XGBoost prediction model
* Uses:

  * Open-Meteo forecasts
  * Local aerodrome weather data
  * Real-time farm sensor data
* Predicts rainfall for the next hour
* Achieves approximately **97% local rain/no-rain prediction accuracy**
* Automatically pauses irrigation when rainfall is expected

---

## 💧 Precision Irrigation

* Sector-wise irrigation control
* Multi-valve architecture
* Automated pump control
* Crop-specific irrigation scheduling
* Water logging detection
* Leak detection

---

## 📱 Android Application

Built using:

* Kotlin
* Jetpack Compose

Features:

* Real-time monitoring
* Live sensor dashboard
* Manual irrigation control
* Emergency stop
* QR-based node onboarding
* Historical analytics
* Push notifications

---

## 🌐 Web Dashboard

Provides administrative and large-scale monitoring capabilities:

* Farm monitoring
* Sensor management
* Historical data visualization
* Government/resource monitoring
* Water usage analytics
* Remote management

---

## ☁ Cloud Infrastructure

Powered by Firebase:

* Real-time Database
* Authentication
* Historical storage
* Device synchronization
* Secure cloud communication

---

# 🧠 Weather Prediction Pipeline

Input data includes:

* Open-Meteo forecast
* Local aerodrome weather
* Humidity
* Pressure
* Wind speed
* Wind direction
* Historical farm sensor readings

↓

Feature Engineering

↓

XGBoost Training

↓

Rainfall Prediction

↓

Automated Irrigation Decision

---

# 🔄 System Workflow

```text
Sensors
      │
      ▼
ESP32 Soil Node
      │
      ▼
Mesh Network
      │
      ▼
Parent Node
      │
      ▼
Firebase Cloud
      │
 ┌────┴────┐
 ▼         ▼
Android   Web Dashboard
      │
      ▼
Weather Prediction Model
      │
      ▼
Smart Irrigation Decisions
```