package com.example.blood_app

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore

class CreateProfileFirsActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var continuarButton: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_firs)

        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        continuarButton = findViewById(R.id.continuar)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        continuarButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val user = hashMapOf(
                    "nombre" to name,
                    "correo" to email,
                    "clave" to password
                )
                db.collection("usuarios")
                    .add(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileLastActivity::class.java))
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
