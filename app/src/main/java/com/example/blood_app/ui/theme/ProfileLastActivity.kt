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
import com.example.blood_app.ui.theme.HistorialActivity


class ProfileLastActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_last)

        profileNameTextView = findViewById(R.id.profileNameTextView)
        profileEmailTextView = findViewById(R.id.profileEmailTextView)

        loadUserProfile()
        val historialButton: Button = findViewById(R.id.historialButton)
        historialButton.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }
        loadUserProfile()
    }

    private fun loadUserProfile() {
        db.collection("usuarios")
            .orderBy("nombre", Query.Direction.DESCENDING) // ¡OJO! Sigue siendo una forma no confiable.
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot) {
                        val nombre = document.getString("nombre")
                        val correo = document.getString("correo")
                        profileNameTextView.text = nombre
                        profileEmailTextView.text = correo
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