package com.example.smartagro.viewmodel
// 📁 com.example.smartagro.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smartagro.data.dataClasses.* // Import all data classes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KisanViewModel : ViewModel() {

    private val _kisanData = MutableStateFlow(KisanData())
    val kisanData: StateFlow<KisanData> = _kisanData

    private val database = FirebaseDatabase.getInstance()
    // Using a dynamic reference key (like "Niranj" or "Nikunj") for better structure
    private val kisanRef = database.getReference("Niranj") // Changed from Kisan1 to Niranj based on the image

    init {
        listenForDataChanges()
    }

    // --- READ FUNCTION (FETCH) ---
    private fun listenForDataChanges() {
        kisanRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(KisanData::class.java)
                if (data != null) {
                    _kisanData.value = data
                    Log.d("Firebase", "Data fetched successfully: ${data.Name}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read value.", error.toException())
            }
        })
    }

    // --- WRITE FUNCTION (SEND/UPDATE) ---
    /**
     * Sends/updates the entire KisanData object to the RTDB under the root key ("Niranj").
     * @param data The complete KisanData object with all fields updated.
     */
    fun updateKisanMetadata(
        newLatitude: Double,
        newLongitude: Double,
        newLocation: String,
        newCrop: String,
        newFarmType: String,
        newName: String,
        parent: Boolean
    ) {
        // 1. Create a Map containing only the keys and values you want to change.
        val metadataUpdates = hashMapOf<String, Any>(
            "Latitude" to newLatitude,
            "Longitude" to newLongitude,
            "Location" to newLocation,
            "Crop" to newCrop,
            "FarmType" to newFarmType,
            "Name" to newName,
            "Parent" to parent
        )

        // 2. Use updateChildren() to apply the map to the current node (kisanRef).
        kisanRef.updateChildren(metadataUpdates)
            .addOnSuccessListener {
                Log.d("Firebase", "Metadata updated successfully!")

                // OPTIONAL: Manually update the StateFlow with the new values
                // for immediate UI reflection, although the listener will fire shortly.
                _kisanData.value = _kisanData.value.copy(
                    Latitude = newLatitude,
                    Longitude = newLongitude,
                    Location = newLocation,
                    Crop = newCrop,
                    FarmType = newFarmType,
                    Name = newName
                )
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update metadata.", e)
            }
    }

    fun updateIrrigationStatus(newStatus: Boolean) {

        val statusMap = hashMapOf<String, Any>(
            "IrrigationStatus" to newStatus
        )

        // Path: Niranj/IrrigationStatus
        kisanRef.updateChildren(statusMap)
            .addOnSuccessListener {
                Log.d("Firebase", "IrrigationStatus updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update IrrigationStatus", e)
            }
    }
    fun updateValveStatus(valve1: Boolean, valve2: Boolean) {

        val statusMap = hashMapOf<String, Any>(
            "Valve1" to valve1,
            "Valve2" to valve2
        )

        // Path: Niranj/IrrigationStatus
        kisanRef.updateChildren(statusMap)
            .addOnSuccessListener {
                Log.d("Firebase", "IrrigationStatus updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update IrrigationStatus", e)
            }
    }
    fun updateNodePosition(nodeName: String, newPosition: Int) {
        val positionMap = hashMapOf<String, Any>(
            nodeName to newPosition
        )

        // Path Example: Niranj/Node1Pos
        kisanRef.updateChildren(positionMap)
            .addOnSuccessListener {
                Log.d("Firebase", "$nodeName updated successfully to position $newPosition")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update $nodeName", e)
            }
    }
}