package com.example.blood_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun CameraPreviewScreen(viewModel: CameraViewModel = viewModel()) {
    val pulseData by viewModel.pulseData.observeAsState()
    val spo2Data by viewModel.spo2Value.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "PPM: ${pulseData ?: "--"}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "SpO₂: ${spo2Data ?: "--"}%", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.saveMeasurement() }) {
            Text("Guardar Medición")
        }
    }
}
