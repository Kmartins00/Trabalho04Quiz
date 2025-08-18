package com.example.quizonline.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizonline.ui.dashboard.DashboardActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Usuário está logado, vai para a tela principal
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            // Usuário não está logado, vai para a tela de login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // Finaliza a SplashActivity para que o usuário não possa voltar para ela
    }
}