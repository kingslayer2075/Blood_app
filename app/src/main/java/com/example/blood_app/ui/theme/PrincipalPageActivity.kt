package com.example.blood_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.ui.theme.PpmActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var ppmTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        val ppmbButton = findViewById<ImageButton>(R.id.ppmbButton)
        val profButton = findViewById<ImageButton>(R.id.profButton)
        val cameraButton = findViewById<ImageButton>(R.id.ButtonP)
        ppmTextView = findViewById(R.id.ppmTextView)

        // Ir a perfil
        profButton.setOnClickListener {
            startActivity(Intent(this, ProfileLastActivity::class.java))
        }

        // Ir a historial
        ppmbButton.setOnClickListener {
            startActivity(Intent(this, PpmActivity::class.java))
        }

        // Ir a cámara
        cameraButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        obtenerUltimoPPM()
    }

    private fun obtenerUltimoPPM() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("ppmData")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val documento = result.documents.first()
                    val ppm = documento.getDouble("ppm")
                    if (ppm != null) {
                        ppmTextView.text = "Último PPM: $ppm"
                    } else {
                        ppmTextView.text = "No se pudo leer el dato"
                    }
                } else {
                    ppmTextView.text = "Sin registros aún"
                }
            }
            .addOnFailureListener { e ->
                Log.e("PrincipalPageActivity", "Error al obtener PPM: ${e.message}")
                ppmTextView.text = "Error al cargar PPM"
            }
    }
}
