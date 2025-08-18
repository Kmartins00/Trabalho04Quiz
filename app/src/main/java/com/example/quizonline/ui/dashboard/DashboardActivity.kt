// Local: ui/dashboard/DashboardActivity.kt
package com.example.quizonline.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizonline.databinding.ActivityDashboardBinding
import com.example.quizonline.ui.auth.LoginActivity
import com.example.quizonline.ui.history.HistoryActivity
import com.example.quizonline.ui.main.MainActivity
import com.example.quizonline.ui.ranking.RankingActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifica se o utilizador está logado. Se não, vai para o Login.
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setupClickListeners()
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.welcomeText.observe(this) { text ->
            binding.welcomeText.text = text
        }

        viewModel.totalScoreText.observe(this) { text ->
            binding.userTotalScoreText.text = text
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.navigateToLogin.observe(this) { navigate ->
            if (navigate) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setupClickListeners() {
        binding.startQuizCard.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.rankingCard.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java))
        }

        binding.historyCard.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.logoutCard.setOnClickListener {
            viewModel.logout()
        }
    }
}
