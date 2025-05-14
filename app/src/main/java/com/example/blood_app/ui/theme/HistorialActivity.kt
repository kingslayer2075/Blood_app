package com.example.blood_app.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.ImageView

class HistorialActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial)

        lineChart = findViewById(R.id.lineChart)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        obtenerDatosDesdeFirebase()
    }

    private fun obtenerDatosDesdeFirebase() {
        val listaPuntos = mutableListOf<Entry>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        Log.d("HistorialActivity", "UID del usuario: $uid")

        // Obtener el documento cuyo ID es el UID del usuario
        db.collection("ppmData")
            .document(uid)  // Aquí obtenemos el documento por el UID del usuario
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Recuperar el valor de ppm
                    val ppm = document.getDouble("ppm")

                    Log.d("HistorialActivity", "ppm recuperado: $ppm en el documento con ID: $uid")

                    // Validar que el valor de ppm sea válido (no nulo y mayor que 0)
                    if (ppm != null && ppm > 0) {
                        listaPuntos.add(Entry(0f, ppm.toFloat())) // Solo un punto ya que el documento tiene un solo valor de ppm

                        val dataSet = LineDataSet(listaPuntos, "PPM")
                        dataSet.color = getColor(R.color.teal_700)
                        dataSet.valueTextColor = getColor(android.R.color.black)

                        val lineData = LineData(dataSet)
                        lineChart.data = lineData
                        lineChart.invalidate()  // Refrescar la gráfica
                    } else {
                        Log.d("HistorialActivity", "ppm inválido o nulo en el documento con ID: $uid")
                    }
                } else {
                    Log.d("HistorialActivity", "No se encontró el documento con el UID: $uid")
                }
            }
            .addOnFailureListener { e ->
                Log.e("HistorialActivity", "Error al obtener datos: ${e.message}")
            }
    }
}
