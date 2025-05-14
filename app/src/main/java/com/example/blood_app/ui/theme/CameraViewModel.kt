package com.example.blood_app

import androidx.lifecycle.*
import kotlinx.coroutines.*
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import java.util.UUID
import com.example.blood_app.Event

//Estado de la camara de tipo mutable, lista y guradado temporal, funcion para validar,
// simular y actualizar los valores de pulso
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

            // Validar el pulso antes de actualizar el valor
            if (validatePulse(simulatedPulse)) {
                _pulseData.postValue(simulatedPulse)
                val newReading = PulseData(simulatedPulse)
                pulseDataList.add(newReading)
                if (pulseDataList.size ==5) {
                    uploadToFirebase(pulseDataList.toList())
                    pulseDataList.clear()
                }
            } else {
                // Si el pulso no es válido, se puede manejar (por ejemplo, registrando un error)
            }
        }
    }

    private fun uploadToFirebase(pulseList: List<PulseData>) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("heart_rate_readings")

        val dataMap = pulseList.map {
            mapOf("value" to it.value, "timestamp" to it.timestamp)
        }

        val entryId = UUID.randomUUID().toString()

        ref.child(entryId).setValue(dataMap)
            .addOnSuccessListener {
                Log.d("Firebase", "Datos subidos correctamente.")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error al subir datos", it)
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
                entry.children.forEach {
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
