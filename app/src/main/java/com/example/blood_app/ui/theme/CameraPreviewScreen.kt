package com.example.blood_app

import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.camera.view.PreviewView
import androidx.compose.ui.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun CameraPreviewScreen(viewModel: CameraViewModel) {
    val pulseData by viewModel.pulseData.observeAsState()
    val isCameraReady by viewModel.isCameraReady.observeAsState(false)

    val context = LocalContext.current  // Se obtiene el contexto fuera del LaunchedEffect para evitar problemas

    // Usamos LaunchedEffect para ejecutar efectos secundarios como el Toast dentro del contexto composable
    LaunchedEffect(pulseData) {
        pulseData?.let {
            if (viewModel.validatePulse(it)) {
                Toast.makeText(context, "Pulse: $it", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Invalid pulse", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isCameraReady == true) {
            Text("Camera is ready. Measuring pulse...", color = Color.Green)
            // Vista previa de la cámara
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        // Configuración de la cámara aquí
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        } else {
            Text("Camera not ready", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.startPulseMeasurement() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Start Measurement")
        }
    }
}

@Preview
@Composable
fun PreviewCamera() {
    val viewModel: CameraViewModel = viewModel()
    CameraPreviewScreen(viewModel = viewModel)
}