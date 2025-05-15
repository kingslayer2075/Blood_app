package com.example.blood_app.ui.theme

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistorialActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var btnBack: ImageView
    private lateinit var descargarButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        lineChart = findViewById(R.id.lineChart)
        btnBack = findViewById(R.id.btnBack)
        descargarButton = findViewById(R.id.descargarButton)

        btnBack.setOnClickListener { finish() }

        descargarButton.setOnClickListener {
            saveChartWithMediaStore()
        }

        obtenerDatosDesdeFirebase()
    }

    private fun obtenerDatosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val listaPuntos = mutableListOf<Entry>()

        db.collection("ppmData")
            .document(uid)
            .collection("registros")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                var index = 0f
                for (document in result) {
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

    private fun saveChartWithMediaStore() {
        if (lineChart.data == null) {
            Toast.makeText(this, "No hay datos para guardar la gráfica", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = "grafica_ppm_${System.currentTimeMillis()}.png"
        val bitmap = lineChart.chartBitmap

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/BloodApp")
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(this, "Gráfica guardada en la galería", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Error al crear archivo", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("HistorialActivity", "Error guardando gráfica: ${e.message}")
            Toast.makeText(this, "Error guardando la gráfica: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}