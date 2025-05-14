package com.example.blood_app.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.*



class HistorialActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        lineChart = findViewById(R.id.lineChart)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish()}

        obtenerDatosDesdeFirebase()
    }

    private fun obtenerDatosDesdeFirebase() {
        val listaPuntos = mutableListOf<Entry>()

        db.collection("mediciones")
            .get()
            .addOnSuccessListener { documents ->
                var index = 0f
                for (document in documents) {
                    val ppm = document.getDouble("ppm") ?: continue
                    listaPuntos.add(Entry(index, ppm.toFloat()))
                    index += 1
                }

                val dataSet = LineDataSet(listaPuntos, "PPM")
                dataSet.color = getColor(R.color.teal_700)
                dataSet.valueTextColor = getColor(android.R.color.black)

                val lineData = LineData(dataSet)
                lineChart.data = lineData
                lineChart.invalidate()
            }
            .addOnFailureListener {
            }
    }
}
