package com.example.cybersecurity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btncrypto = findViewById<ImageView>(R.id.btncrypto)

        btncrypto.setOnClickListener {
           val intent = Intent(this, UnitActivity::class.java)
            startActivity(intent)
        }

    }
}