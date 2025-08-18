package com.example.quizonline.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizonline.ui.dashboard.DashboardActivity
import com.example.quizonline.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmailAddressLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Salva o estado de logado (SharedPreferences) conforme citado no documento de orientação
                        val user = task.result.user
                        saveLoginState(user!!.uid)

                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    //Nome da LLM: Gemini , utilizamos como recurso para correções de inconsistência com o XML

    private fun saveLoginState(userId: String) {
        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("userId", userId)
        editor.apply()
    }
}