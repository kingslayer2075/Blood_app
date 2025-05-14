package com.example.blood_app

import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import com.example.blood_app.ui.theme.PpmActivity


class PrincipalPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal_page)

        val ppmbButton = findViewById<ImageButton>(R.id.ppmbButton)
        val profButton = findViewById<ImageButton>(R.id.profButton)
        
        profButton.setOnClickListener {
            val intent = Intent(this, ProfileLastActivity::class.java)
            startActivity(intent)
        }
        
        ppmbButton.setOnClickListener {
            val intent = Intent(this, PpmActivity::class.java)
            startActivity(intent)
        }
    }

}
