package com.example.blood_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var subtitleText: TextView
    private lateinit var emailInput: EditText
    private lateinit var continueButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var googleLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // Asegura que uses un tema basado en AppCompat (ya configurado en styles.xml y AndroidManifest.xml)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_create)

        // Inicializar vistas
        logoImage = findViewById(R.id.logoImage)
        titleText = findViewById(R.id.titleText)
        subtitleText = findViewById(R.id.subtitleText)
        emailInput = findViewById(R.id.emailInput)
        continueButton = findViewById(R.id.continueButton)
        createAccountButton = findViewById(R.id.createAccountButton)
        googleLoginButton = findViewById(R.id.googleLoginButton)

        // Lógica de botones
        continueButton.setOnClickListener {
            val email = emailInput.text.toString()
            Toast.makeText(this, "Email ingresado: $email", Toast.LENGTH_SHORT).show()
        }

        createAccountButton.setOnClickListener {
            Toast.makeText(this, "Crear cuenta clicado", Toast.LENGTH_SHORT).show()
        }

        googleLoginButton.setOnClickListener {
            Toast.makeText(this, "Google login clicado", Toast.LENGTH_SHORT).show()
        }
    }
}
