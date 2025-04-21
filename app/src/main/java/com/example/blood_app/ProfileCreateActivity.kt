package com.example.blood_app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.blood_app.ui.theme.IniciasSesionActivity


class ProfileCreateActivity : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    private lateinit var continueButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var googleLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_create)

        logoImage = findViewById(R.id.logoImage)
        continueButton = findViewById(R.id.continueButton)
        createAccountButton = findViewById(R.id.createAccountButton)
        googleLoginButton = findViewById(R.id.googleLoginButton)

        continueButton.setOnClickListener {
            Log.d("CreateAccount", "Botón continuar presionado")
            val intent = Intent(this, IniciasSesionActivity::class.java)
            startActivity(intent)
        }

        createAccountButton.setOnClickListener {
            Log.d("CreateAccount", "Botón crear cuenta presionado")
            val intent = Intent(this, CreateProfileFirsActivity::class.java)
            startActivity(intent)
        }

        googleLoginButton.setOnClickListener {
            Log.d("CreateAccount", "Google login clicado")
            Toast.makeText(this, "Google login clicado", Toast.LENGTH_SHORT).show()
        }
    }
}
