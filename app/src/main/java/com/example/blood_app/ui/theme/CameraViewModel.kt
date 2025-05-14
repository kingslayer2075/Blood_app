package com.example.blood_app

import androidx.lifecycle.*
import kotlinx.coroutines.*
import androidx.compose.runtime.mutableStateListOf

class CameraViewModel : ViewModel() {

    // MutableLiveData para almacenar el pulso actual
    private val _pulseData = MutableLiveData<Int>()
    val pulseData: LiveData<Int> get() = _pulseData

    // MutableLiveData para el estado de la cámara (si está lista o no)
    private val _isCameraReady = MutableLiveData<Boolean>()
    val isCameraReady: LiveData<Boolean> get() = _isCameraReady

    // Lista temporal para guardar los datos de pulso
    private val pulseDataList = mutableStateListOf<PulseData>()

    // Getter para el pulso actual
    val pulseValue: Int?
        get() = _pulseData.value

    // Getter para la lista de pulsos registrados
    val pulseList: List<PulseData>
        get() = pulseDataList.toList()

    // Función para validar el pulso
    fun validatePulse(pulse: Int): Boolean {
        // Validación básica: asegurar que el pulso esté en un rango razonable (60-100)
        return pulse in 60..100
    }

    // Función para simular la medición del pulso
    fun startPulseMeasurement() {
        viewModelScope.launch {
            val simulatedPulse = withContext(Dispatchers.IO) {
                (60..100).random() // Simula un valor de pulso aleatorio entre 60 y 100
            }

            // Validar el pulso antes de actualizar el valor
            if (validatePulse(simulatedPulse)) {
                _pulseData.postValue(simulatedPulse) // Actualizar el valor del pulso
                pulseDataList.add(PulseData(simulatedPulse)) // Agregar a la lista de pulsos
            } else {
                // Si el pulso no es válido, puedes manejarlo (por ejemplo, registrando un error)
                // Aquí no hacemos nada, pero puedes implementar alguna lógica de manejo de errores
            }
        }
    }

    // Función para establecer si la cámara está lista o no
    fun setCameraReady(isReady: Boolean) {
        _isCameraReady.postValue(isReady)
    }
}
