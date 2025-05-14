package com.example.blood_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.compose.ui.semantics.text
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class CreateProfileFirsActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var bloodTypeInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var continuarButton: Button
    private lateinit var btnBack: ImageView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_firs)

        nameInput = findViewById(R.id.nameInput)
        lastNameInput = findViewById(R.id.lastNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        bloodTypeInput = findViewById(R.id.bloodTypeInput)
        weightInput = findViewById(R.id.weightInput)
        heightInput = findViewById(R.id.heightInput)
        ageInput = findViewById(R.id.ageInput)
        continuarButton = findViewById(R.id.continuar)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        continuarButton.setOnClickListener {
            createFirestoreUser()

        }
    }
    private fun createFirestoreUser() {
        val name = nameInput.text.toString().trim()
        val lastName = lastNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val bloodType = bloodTypeInput.text.toString().trim()
        val weightStr = weightInput.text.toString().trim()
        val heightStr = heightInput.text.toString().trim()
        val ageStr = ageInput.text.toString().trim()

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
            bloodType.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val weight = weightStr.toDoubleOrNull()
        val height = heightStr.toDoubleOrNull()
        val age = ageStr.toIntOrNull()

        if (weight == null || height == null || age == null) {
            Toast.makeText(this, "Por favor, introduce valores numéricos válidos para peso, altura y edad.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = hashMapOf(
            "nombre" to name,
            "apellido" to lastName,
            "correo" to email,
            "clave" to password,
            "tipoSangre" to bloodType,
            "peso" to weight,
            "altura" to height,
            "edad" to age
        )

        db.collection("usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Documento de usuario añadido con ID: ${documentReference.id}")
                Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, ProfileLastActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al añadir documento de usuario", e)
                Toast.makeText(this, "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    companion object {
        private const val TAG = "CreateProfileActivity"
    }
}
