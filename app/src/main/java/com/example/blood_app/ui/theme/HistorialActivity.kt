package com.example.blood_app.ui.theme

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistorialActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var btnBack: ImageView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        lineChart = findViewById(R.id.lineChart)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        obtenerDatosDesdeFirebase()
    }

    private fun obtenerDatosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val listaPuntos = mutableListOf<Entry>()

        db.collection("ppmData")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Orden descendente
            .limit(15) // Solo los 15 más recientes
            .get()
            .addOnSuccessListener { result ->
                val reversed = result.reversed() // Revertir para orden cronológico
                var index = 0f
                for (document in reversed) {
                    val ppm = document.getDouble("ppm")
                    if (ppm != null) {
                        listaPuntos.add(Entry(index, ppm.toFloat()))
                        index++
                    }
                }

                if (listaPuntos.isNotEmpty()) {
                    val dataSet = LineDataSet(listaPuntos, "Historial de PPM")
                    dataSet.color = getColor(R.color.teal_700)
                    dataSet.setCircleColor(Color.BLACK)
                    dataSet.valueTextColor = Color.BLACK
                    dataSet.lineWidth = 2f
                    dataSet.circleRadius = 4f
                    dataSet.setDrawValues(true)

                    val lineData = LineData(dataSet)
                    lineChart.data = lineData
                    lineChart.description.text = "Lecturas de PPM"
                    lineChart.animateX(1000)
                    lineChart.invalidate()
                } else {
                    Log.d("HistorialActivity", "No se encontraron datos de PPM.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("HistorialActivity", "Error al obtener datos: ${e.message}")
            }
    }
}
