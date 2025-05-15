package com.example.blood_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
    private val auth = FirebaseAuth.getInstance()

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
            registerUser()
        }
    }

    private fun registerUser() {
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
            Toast.makeText(this, "Introduce valores válidos para peso, altura y edad.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "uid" to uid,
                        "nombre" to name,
                        "apellido" to lastName,
                        "correo" to email,
                        "tipoSangre" to bloodType,
                        "peso" to weight,
                        "altura" to height,
                        "edad" to age
                    )

                    db.collection("usuarios").document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            val ppmData = hashMapOf("uid" to uid)

                            db.collection("ppmData").document(uid)
                                .set(ppmData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Usuario registrado y PPM Data creado", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, ProfileLastActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error al guardar en ppmData: ${e.message}")
                                    Toast.makeText(this, "Error al crear PPM Data", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error al guardar en Firestore", e)
                            Toast.makeText(this, "Error en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Error al crear usuario: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val TAG = "CreateProfileActivity"
    }
}
