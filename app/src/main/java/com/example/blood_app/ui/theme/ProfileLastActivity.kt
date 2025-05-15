package com.example.blood_app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import com.example.blood_app.ui.theme.HistorialActivity


class ProfileLastActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var profileLastNameTextView: TextView
    private lateinit var profileBloodTextView: TextView
    private lateinit var profileWeightTextView: TextView
    private lateinit var profileHeightTextView: TextView
    private lateinit var profileAgeTextView: TextView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_last)

        profileNameTextView = findViewById(R.id.profileNameTextView)
        profileEmailTextView = findViewById(R.id.profileEmailTextView)
        profileLastNameTextView = findViewById(R.id.profileLastNameTextView)
        profileAgeTextView = findViewById(R.id.profileAgeTextView)
        profileBloodTextView = findViewById(R.id.profileBloodTextView)
        profileHeightTextView = findViewById(R.id.profileHeightTextView)
        profileWeightTextView = findViewById(R.id.profileWeightTextView)
        val refreshButton: Button = findViewById(R.id.refreshButton)
        val historialButton: Button = findViewById(R.id.historialButton)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener{finish()}


        refreshButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }


        historialButton.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }
        loadUserProfile()
    }

    private fun loadUserProfile() {
        db.collection("usuarios")
            .orderBy("nombre", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot) {
                        val nombre = document.getString("nombre")
                        val correo = document.getString("correo")
                        val apellido = document.getString("apellido")
                        val tipoSangre = document.getString("tipoSangre")
                        val peso = document.getDouble("peso")
                        val altura = document.getDouble("altura")
                        val edad = document.getLong("edad")?.toInt()
                        profileNameTextView.text = nombre
                        profileEmailTextView.text = correo
                        profileLastNameTextView.text = apellido
                        profileBloodTextView.text = tipoSangre
                        profileWeightTextView.text = "${peso ?:0.0} kg"
                        profileHeightTextView.text = "${altura ?: 0.0} cm"
                        profileAgeTextView.text = "${edad ?: 0}"
                        return@addOnSuccessListener
                    }
                } else {
                    Toast.makeText(this, "No se encontraron usuarios.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar el perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error al cargar usuarios", e)
            }
    }
}