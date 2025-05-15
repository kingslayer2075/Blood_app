package com.example.blood_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CameraViewModel : ViewModel() {
    private val _pulseData = MutableLiveData<Int?>()
    val pulseData: LiveData<Int?> = _pulseData

    private val _spo2Value = MutableLiveData<Int?>()
    val spo2Value: LiveData<Int?> = _spo2Value

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    fun startMeasurement() {
        _pulseData.value = (60..100).random()
        _spo2Value.value = (95..100).random()
    }

    fun saveMeasurement() {
        val uid = auth.currentUser?.uid ?: return
        val timestamp = System.currentTimeMillis()
        val pulse = _pulseData.value
        val spo2 = _spo2Value.value
        if (pulse != null) {
            val ppmRecord = mapOf(
                "ppm" to pulse,
                "timestamp" to timestamp
            )
            db.collection("ppmData")
                .document(uid)
                .collection("registros")
                .add(ppmRecord)
        }
        if (spo2 != null) {
            val spo2Record = mapOf(
                "spo2" to spo2,
                "timestamp" to timestamp
            )
            db.collection("spo2Data")
                .document(uid)
                .collection("registros")
                .add(spo2Record)
        }
    }

    init {
        startMeasurement()
    }
}
