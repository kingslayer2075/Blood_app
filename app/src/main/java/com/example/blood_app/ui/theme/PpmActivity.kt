package com.example.blood_app.ui.theme

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blood_app.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PpmActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ppm)

        lineChart = findViewById(R.id.lineChart)

        val datos = listOf(
            Entry(0f, 70f),
            Entry(1f, 75f),
            Entry(2f, 80f),
            Entry(3f, 78f),
            Entry(4f, 76f)
        )

        val dataSet = LineDataSet(datos, "PPM")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(Color.RED)
        dataSet.circleRadius = 4f

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.description = Description().apply { text = "Frecuencia PPM"; textColor = Color.GRAY }
        lineChart.invalidate()
    }
}
