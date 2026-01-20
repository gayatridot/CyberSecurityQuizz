package com.example.cybersecurity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UnitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unit)

        val subject = intent.getStringExtra("subject") ?: "Cryptography"

        setupUnitClick(R.id.u_1, subject, "Unit 1")
        setupUnitClick(R.id.u_2, subject, "Unit 2")
        setupUnitClick(R.id.u_3, subject, "Unit 3")
        setupUnitClick(R.id.u_4, subject, "Unit 4")
        setupUnitClick(R.id.u_5, subject, "Unit 5")
        setupUnitClick(R.id.u_6, subject, "Unit 6")
    }

    private fun setupUnitClick(id: Int, subject: String, unit: String) {
        findViewById<TextView>(id).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("subject", subject)
            intent.putExtra("unit", unit)
            startActivity(intent)
        }
    }
}