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
    private lateinit var spo2TextView: TextView
    private lateinit var spo2Chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        // Botones de navegación
        findViewById<ImageButton>(R.id.profButton)
            .setOnClickListener { startActivity(Intent(this, ProfileLastActivity::class.java)) }

        findViewById<ImageButton>(R.id.ppmbButton)
            .setOnClickListener { startActivity(Intent(this, PpmActivity::class.java)) }

        findViewById<ImageButton>(R.id.ButtonP)
            .setOnClickListener { startActivity(Intent(this, CameraActivity::class.java)) }

        // Referencias UI
        spo2TextView = findViewById(R.id.spo2TextView)
        spo2Chart    = findViewById(R.id.spo2Chart)

        // Carga datos
        showLatestSpO2()
        loadSpO2Sparkline()
    }

    private fun showLatestSpO2() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val spo2 = result.documents.firstOrNull()?.getDouble("spo2")
                spo2TextView.text = if (spo2 != null) "Último SpO₂: ${spo2.toInt()}%"
                else "Sin registros de SpO₂"
            }
            .addOnFailureListener {
                spo2TextView.text = "Error al cargar SpO₂"
            }
    }

    private fun loadSpO2Sparkline() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)  // solo las 5 más recientes
            .get()
            .addOnSuccessListener { result ->
                // revertir para que queden cronológicas
                val entries = result.documents
                    .asReversed()
                    .mapIndexedNotNull { idx, doc ->
                        doc.getDouble("spo2")?.let { Entry(idx.toFloat(), it.toFloat()) }
                    }

                val dataSet = LineDataSet(entries, "").apply {
                    setDrawValues(false)      // sin valores encima
                    color = resources.getColor(R.color.purple_500, null)
                    lineWidth = 2f
                    setDrawCircles(false)     // sparkline sin puntos
                }

                spo2Chart.apply {
                    data = LineData(dataSet)
                    description.isEnabled = false
                    legend.isEnabled = false
                    xAxis.run {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        setDrawLabels(false)
                    }
                    axisLeft.run {
                        setDrawGridLines(false)
                        setDrawLabels(false)
                    }
                    axisRight.isEnabled = false
                    setTouchEnabled(false)
                    invalidate()
                }
            }
            .addOnFailureListener { e ->
                Log.e("PrincipalPageAct", "Error gráf. SpO₂: ${e.message}")
            }
    }
}
