package com.example.blood_app

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import androidx.compose.runtime.mutableStateListOf

class CameraViewModel : ViewModel() {

    private val _pulseData = MutableLiveData<Int>()
    val pulseData: LiveData<Int> get() = _pulseData

    private val _isCameraReady = MutableLiveData<Boolean>()
    val isCameraReady: LiveData<Boolean> get() = _isCameraReady

    private val pulseDataList = mutableStateListOf<PulseData>()

    val pulseValue: Int?
        get() = _pulseData.value

    val pulseList: List<PulseData>
        get() = pulseDataList.toList()

    private val uploadSuccessEvent = MutableLiveData<Event<Boolean>>()

    fun validatePulse(pulse: Int): Boolean {
        return pulse in 60..100
    }

    fun startPulseMeasurement() {
        viewModelScope.launch {
            val simulatedPulse = withContext(Dispatchers.IO) {
                (60..100).random()
            }

            if (validatePulse(simulatedPulse)) {
                _pulseData.postValue(simulatedPulse)
                val newReading = PulseData(simulatedPulse)
                pulseDataList.add(newReading)

                if (pulseDataList.size == 1) {
                    uploadToFirebase(newReading)
                    pulseDataList.clear()
                }
            }
        }
    }

    private fun uploadToFirebase(pulse: PulseData) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val data = mapOf(
            "ppm" to pulse.value,
            "timestamp" to pulse.timestamp
        )

        db.collection("ppmData")
            .document(uid)
            .collection("registros")
            .add(data)
            .addOnSuccessListener {
                Log.d("Firestore", "PPM registrado correctamente.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al subir PPM: ${e.message}")
            }
    }

    fun setCameraReady(isReady: Boolean) {
        _isCameraReady.postValue(isReady)
    }

    fun fetchPulseDataFromFirebase(onDataReady: (List<PulseData>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("heart_rate_readings")

        ref.get().addOnSuccessListener { snapshot ->
            val pulseList = mutableListOf<PulseData>()
            snapshot.children.forEach { entry ->
                entry.children.forEach { it: DataSnapshot ->
                    val value = it.child("value").getValue(Int::class.java)
                    val timestamp = it.child("timestamp").getValue(Long::class.java)
                    if (value != null && timestamp != null) {
                        pulseList.add(PulseData(value, timestamp))
                    }
                }
            }
            onDataReady(pulseList)
        }.addOnFailureListener {
            Log.e("Firebase", "Error al leer datos", it)
        }
    }
}
