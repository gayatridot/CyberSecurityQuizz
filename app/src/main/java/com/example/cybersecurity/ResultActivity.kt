package com.example.cybersecurity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        val scoreText = findViewById<TextView>(R.id.scoreText)
        val progressBar = findViewById<ProgressBar>(R.id.circularProgressBar)
        val leaderboardBtn = findViewById<Button>(R.id.leaderboardBtn)
        val homeBtn = findViewById<Button>(R.id.homeBtn)

        scoreText.text = "$score / $total"
        val progress = if (total > 0) (score * 100) / total else 0
        progressBar.progress = progress

        saveScoreToLeaderboard(score)

        leaderboardBtn.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun saveScoreToLeaderboard(score: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = it.email?.replace(".", "_") ?: "unknown"
            val ref = FirebaseDatabase.getInstance().getReference("Leaderboard").child(email)
            
            ref.child("score").get().addOnSuccessListener { snapshot ->
                val currentBest = snapshot.getValue(Int::class.java) ?: 0
                if (score > currentBest) {
                    ref.child("score").setValue(score)
                    ref.child("email").setValue(it.email)
                }
            }
        }
    }
}