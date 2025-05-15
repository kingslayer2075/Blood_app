package com.example.blood_app.ui.theme

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PpmActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var btnBack: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var reiniciarButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val listaPuntos = mutableListOf<Entry>()
    private val valoresPPM = mutableListOf<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ppm)

        lineChart = findViewById(R.id.lineChart)
        btnBack = findViewById(R.id.btnBack)
        profileNameTextView = findViewById(R.id.profileNameTextView)
        reiniciarButton = findViewById(R.id.Reiniciar)

        btnBack.setOnClickListener { finish() }

        reiniciarButton.setOnClickListener {
            reiniciarGrafica()
        }

        obtenerDatosDesdeFirebase()
    }

    private fun obtenerDatosDesdeFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        listaPuntos.clear()
        valoresPPM.clear()

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
                        valoresPPM.add(ppm.toFloat())
                        index++
                    }
                }

                if (listaPuntos.isNotEmpty()) {
                    actualizarGrafica(listaPuntos)

                    val mediana = calcularMediana(valoresPPM)
                    mostrarConsejo(mediana)
                } else {
                    Log.d("PpmActivity", "No se encontraron datos de PPM.")
                    profileNameTextView.text = "No hay datos suficientes para dar un consejo."
                    lineChart.clear()
                }
            }
            .addOnFailureListener { e ->
                Log.e("PpmActivity", "Error al obtener datos: ${e.message}")
                profileNameTextView.text = "Error al obtener datos."
                lineChart.clear()
            }
    }

    private fun actualizarGrafica(datos: List<Entry>) {
        val dataSet = LineDataSet(datos, "PPM")
        dataSet.color = Color.RED
        dataSet.setCircleColor(Color.RED)
        dataSet.valueTextColor = Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(true)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.description = Description().apply {
            text = "Frecuencia PPM"
            textColor = Color.GRAY
        }

        lineChart.animateX(1000)
        lineChart.invalidate()
    }

    private fun reiniciarGrafica() {
        if (listaPuntos.isNotEmpty()) {
            val ultimoPunto = listaPuntos.last()
            val nuevaLista = listOf(Entry(0f, ultimoPunto.y))
            valoresPPM.clear()
            valoresPPM.add(ultimoPunto.y)

            actualizarGrafica(nuevaLista)

            mostrarConsejo(ultimoPunto.y)
        }
    }

    private fun calcularMediana(lista: List<Float>): Float {
        val ordenada = lista.sorted()
        return if (ordenada.size % 2 == 0) {
            val mid = ordenada.size / 2
            (ordenada[mid - 1] + ordenada[mid]) / 2
        } else {
            ordenada[ordenada.size / 2]
        }
    }

    private fun mostrarConsejo(mediana: Float) {
        profileNameTextView.text = when {
            mediana < 60 -> "Tu frecuencia cardíaca es baja. Consulta a tu médico si tienes síntomas."
            mediana <= 100 -> "Tu frecuencia está dentro del rango normal. ¡Sigue así!"
            else -> "Frecuencia alta. Podrías estar estresado o haciendo esfuerzo físico."
        }
    }
}