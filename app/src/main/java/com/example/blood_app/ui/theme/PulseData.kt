package com.example.blood_app

data class PulseData(
    val value: Int,
    val timestamp: Long = System.currentTimeMillis()
)
