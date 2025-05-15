package com.example.blood_app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var bloodTypeInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var heightInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var continuarBtn: Button
    private lateinit var btnBack: ImageView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        nameInput = findViewById(R.id.nameInput)
        lastNameInput = findViewById(R.id.lastNameInput)
        bloodTypeInput = findViewById(R.id.bloodTypeInput)
        weightInput = findViewById(R.id.weightInput)
        heightInput = findViewById(R.id.heightInput)
        ageInput = findViewById(R.id.ageInput)
        continuarBtn = findViewById(R.id.continuar)
        btnBack = findViewById(R.id.btnBack)

        val uid = auth.currentUser?.uid

        if (uid != null) cargarDatos(uid)

        continuarBtn.setOnClickListener {
            if (uid != null) {
                guardarDatos(uid)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatos(uid: String) {
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    nameInput.setText(document.getString("nombre"))
                    lastNameInput.setText(document.getString("apellido"))
                    bloodTypeInput.setText(document.getString("tipoSangre"))
                    weightInput.setText(document.getDouble("peso")?.toString())
                    heightInput.setText(document.getDouble("altura")?.toString())
                    ageInput.setText(document.getLong("edad")?.toString())
                }
            }
    }

    private fun guardarDatos(uid: String) {
        val data = hashMapOf<String, Any?>(
            "nombre" to nameInput.text.toString(),
            "apellido" to lastNameInput.text.toString(),
            "tipoSangre" to bloodTypeInput.text.toString(),
            "peso" to weightInput.text.toString().toDoubleOrNull(),
            "altura" to heightInput.text.toString().toDoubleOrNull(),
            "edad" to ageInput.text.toString().toIntOrNull()
        ).filterValues { it != null }

        db.collection("usuarios").document(uid)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileLastActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }
}
