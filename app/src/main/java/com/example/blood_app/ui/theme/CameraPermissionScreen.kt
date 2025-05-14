package com.example.blood_app

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext

@Composable
fun CameraPermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionState = remember { mutableStateOf(false) }

    // Revisa si ya está concedido el permiso
    LaunchedEffect(Unit) {
        permissionState.value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Lanzador para solicitar permiso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionState.value = isGranted
        if (isGranted) onPermissionGranted()
    }

    if (permissionState.value) {
        onPermissionGranted()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Se necesita permiso para usar la cámara",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text("Solicitar Permiso")
            }
        }
    }
}
