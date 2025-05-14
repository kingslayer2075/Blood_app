package com.example.blood_app


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraPulseScreen(viewModel: CameraViewModel) {
    val pulseData = viewModel.pulseData.observeAsState(initial = 0)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Valor actual
        Text("Pulso actual: ${pulseData.value} BPM")

        Spacer(modifier = Modifier.height(16.dp))

        // Historial
        Text("Historial de pulsos:")

        Column {
            viewModel.pulseList.forEach {
                Text("• ${it.value} BPM @ ${formatTimestamp(it.timestamp)}")
            }
        }
    }
}

// Formatea la hora
fun formatTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun PreviewCameraPulseScreen() {
    // ⚠️ OJO: viewModel() no funciona bien en @Preview
    // Esto solo es válido si tenés un entorno de prueba o mock
    // Podés comentar esta parte si no la necesitás
    // CameraPulseScreen(viewModel = viewModel())
}
