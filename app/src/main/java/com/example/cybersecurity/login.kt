package com.example.cybersecurity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnlog)
        val tvSignup = findViewById<TextView>(R.id.signup)
        val etEmail = findViewById<EditText>(R.id.email)
        val etPassword = findViewById<EditText>(R.id.password)

        tvSignup.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val emailS = etEmail.text.toString().trim()
            val passwordS = etPassword.text.toString().trim()

            if (emailS.isNotEmpty() && passwordS.isNotEmpty()) {
                loginUser(emailS, passwordS)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(emailS: String, passwordS: String) {
        auth.signInWithEmailAndPassword(emailS, passwordS)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                        userRef.get().addOnSuccessListener { snapshot ->
                            // Map Firebase data to User object
                            val user = snapshot.getValue(User::class.java)
                            
                            Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_SHORT).show()
                            
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}