package com.example.blood_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.ui.theme.PpmActivity


class PrincipalPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        val ppmbButton = findViewById<ImageButton>(R.id.ppmbButton)
        val profButton = findViewById<ImageButton>(R.id.profButton)
        val cameraButton = findViewById<ImageButton>(R.id.ButtonP)  // Aquí se obtiene el botón de la cámara

        // Abrir la actividad de 'ProfileLastActivity'
        profButton.setOnClickListener {
            val intent = Intent(this, ProfileLastActivity::class.java)
            startActivity(intent)
        }

        // Abrir la actividad de 'PpmActivity'
        ppmbButton.setOnClickListener {
            val intent = Intent(this, PpmActivity::class.java)
            startActivity(intent)
        }

        // Abrir la actividad de la cámara ('CameraActivity')
        cameraButton.setOnClickListener {
            val cameraIntent = Intent(this, CameraActivity::class.java)  // Aquí abre CameraActivity
            startActivity(cameraIntent)
        }
    }
}
