package com.example.blood_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CameraPulseScreen(viewModel: CameraViewModel) {
    val pulseData = viewModel.pulseData.observeAsState(initial = 0)
    val spo2Data = viewModel.spo2Value.observeAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pulso actual: ${pulseData.value} PPM")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Oxígeno en sangre (SpO₂): ${spo2Data.value ?: "--"} %")
    }
}
