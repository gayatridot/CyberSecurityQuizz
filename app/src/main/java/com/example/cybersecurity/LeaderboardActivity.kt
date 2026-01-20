package com.example.cybersecurity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LeaderboardAdapter
    private val list = mutableListOf<LeaderboardEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        recyclerView = findViewById(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LeaderboardAdapter(list)
        recyclerView.adapter = adapter

        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        val ref = FirebaseDatabase.getInstance().getReference("Leaderboard")
        // Order by score descending
        ref.orderByChild("score").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (data in snapshot.children) {
                    val entry = data.getValue(LeaderboardEntry::class.java)
                    entry?.let { list.add(it) }
                }
                list.reverse() // Since orderByChild is ascending
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LeaderboardActivity, "Failed to load leaderboard", Toast.LENGTH_SHORT).show()
            }
        })
    }
}