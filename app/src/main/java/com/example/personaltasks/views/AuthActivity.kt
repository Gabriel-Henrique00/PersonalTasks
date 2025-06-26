package com.example.personaltasks.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.personaltasks.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        showProgressBar()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro bem-sucedido.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("AuthActivity", "createUserWithEmailAndPassword:failure", task.exception)
                    Toast.makeText(this, "Falha no registro: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        showProgressBar()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login bem-sucedido.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("AuthActivity", "signInWithEmailAndPassword:failure", task.exception)
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}