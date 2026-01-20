package com.example.cybersecurity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class QuizActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var timerText: TextView
    private lateinit var submitBtn: Button
    private lateinit var adapter: QuizAdapter
    private val questionList = mutableListOf<Question>()
    private var score = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        recyclerView = findViewById(R.id.quizRecyclerView)
        timerText = findViewById(R.id.timerText)
        submitBtn = findViewById(R.id.submitBtn)

        val subject = intent.getStringExtra("subject") ?: "General"
        val unit = intent.getStringExtra("unit") ?: "Unit 1"

        findViewById<TextView>(R.id.quizTitle).text = "$subject - $unit"

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = QuizAdapter(questionList) { position, isCorrect ->
            if (isCorrect) score++
        }
        recyclerView.adapter = adapter

        fetchQuestions(subject, unit)
        startTimer(30 * 60 * 1000L) // 30 minutes

        submitBtn.setOnClickListener {
            finishQuiz()
        }
    }

    private fun fetchQuestions(subject: String, unit: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Questions").child(subject).child(unit)
        ref.limitToFirst(20).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                for (data in snapshot.children) {
                    val question = data.getValue(Question::class.java)
                    question?.let { questionList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, "Failed to load questions", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    private fun finishQuiz() {
        timer?.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("total", questionList.size)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}