package com.example.blood_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.ui.theme.PpmActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var ppmTextView: TextView
    private lateinit var spo2TextView: TextView
    private lateinit var spo2Chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        val ppmbButton = findViewById<ImageButton>(R.id.ppmbButton)
        val profButton = findViewById<ImageButton>(R.id.profButton)
        val cameraButton = findViewById<ImageButton>(R.id.ButtonP)

        ppmTextView = findViewById(R.id.ppmTextView)
        spo2TextView = findViewById(R.id.spo2TextView)
        spo2Chart = findViewById(R.id.spo2Chart)

        profButton.setOnClickListener {
            startActivity(Intent(this, ProfileLastActivity::class.java))
        }

        ppmbButton.setOnClickListener {
            startActivity(Intent(this, PpmActivity::class.java))
        }

        cameraButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        obtenerUltimoPPM()
        obtenerUltimoSpO2()
        cargarGraficaSpO2()
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
                val documento = result.documents.firstOrNull()
                val ppm = documento?.getDouble("ppm")
                ppmTextView.text = if (ppm != null) "Último PPM: $ppm" else "No se pudo leer el dato"
            }
            .addOnFailureListener {
                ppmTextView.text = "Error al cargar PPM"
            }
    }

    private fun obtenerUltimoSpO2() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val documento = result.documents.firstOrNull()
                val spo2 = documento?.getDouble("spo2")
                spo2TextView.text = if (spo2 != null) "Último SpO₂: $spo2%" else "No se pudo leer SpO₂"
            }
            .addOnFailureListener {
                spo2TextView.text = "Error al cargar SpO₂"
            }
    }

    private fun cargarGraficaSpO2() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(15)
            .get()
            .addOnSuccessListener { result ->
                val entries = result.documents.reversed().mapIndexedNotNull { index, doc ->
                    doc.getDouble("spo2")?.let { Entry(index.toFloat(), it.toFloat()) }
                }

                val dataSet = LineDataSet(entries, "SpO₂")
                dataSet.color = resources.getColor(R.color.purple_500, null)
                dataSet.valueTextColor = resources.getColor(R.color.black, null)

                val lineData = LineData(dataSet)
                spo2Chart.data = lineData
                spo2Chart.description.isEnabled = false
                spo2Chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                spo2Chart.invalidate()
            }
            .addOnFailureListener {
                Log.e("PrincipalPageActivity", "Error al cargar gráfica SpO₂: ${it.message}")
            }
    }
}
