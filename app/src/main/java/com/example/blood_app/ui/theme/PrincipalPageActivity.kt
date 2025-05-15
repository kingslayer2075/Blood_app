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
import com.google.firebase.firestore.Query

class PrincipalPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var spo2TextView: TextView
    private lateinit var spo2Chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        findViewById<ImageButton>(R.id.profButton)
            .setOnClickListener { startActivity(Intent(this, ProfileLastActivity::class.java)) }

        findViewById<ImageButton>(R.id.ppmbButton)
            .setOnClickListener { startActivity(Intent(this, PpmActivity::class.java)) }

        findViewById<ImageButton>(R.id.ButtonP)
            .setOnClickListener { startActivity(Intent(this, CameraActivity::class.java)) }

        spo2TextView = findViewById(R.id.spo2TextView)
        spo2Chart = findViewById(R.id.spo2Chart)

        listenForLatestSpO2()
        listenForSpO2GraphUpdates()
    }

    private fun listenForLatestSpO2() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    spo2TextView.text = "Error al cargar SpO₂"
                    Log.w("PrincipalPage", "Error escuchando SpO2: ${e.message}")
                    return@addSnapshotListener
                }

                val spo2 = snapshot?.documents?.firstOrNull()?.getDouble("spo2")
                spo2TextView.text = if (spo2 != null) "Último SpO₂: ${spo2.toInt()}%" else "Sin registros de SpO₂"
            }
    }

    private fun listenForSpO2GraphUpdates() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("spo2Data")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("PrincipalPage", "Error gráf. SpO₂: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val entries = snapshot.documents
                        .asReversed()
                        .mapIndexedNotNull { idx, doc ->
                            doc.getDouble("spo2")?.let { Entry(idx.toFloat(), it.toFloat()) }
                        }

                    if (entries.isNotEmpty()) {
                        val dataSet = LineDataSet(entries, "").apply {
                            setDrawValues(false)
                            color = resources.getColor(R.color.purple_500, null)
                            lineWidth = 2f
                            setDrawCircles(false)
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
                }
            }
    }
}
