package com.example.blood_app

import androidx.lifecycle.LiveData // Importa LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartRateAnalyzer : ViewModel() {

    private val _heartRate = MutableLiveData<Int>()
    val heartRate: LiveData<Int> get() = _heartRate

    fun analyzeHeartRate(rawData: ByteArray) {
        viewModelScope.launch {
            val rate = withContext(Dispatchers.IO) {
                // Analizar datos aquí (procesar la imagen o los datos de la cámara)
                80 // Este es un valor simulado de pulso
            }
            _heartRate.postValue(rate)
        }
    }
}
