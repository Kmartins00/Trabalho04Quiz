package com.example.quizonline.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizonline.databinding.ActivityMainBinding
import com.example.quizonline.ui.dashboard.DashboardActivity
import com.example.quizonline.ui.history.HistoryActivity

class MainActivity : AppCompatActivity() {
    //Nome da LLM: Gemini , utilizamos como recurso para auxílio na correção de bugs gerados por códigos incorretos.


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var quizListAdapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

    }

    private fun setupClickListeners() {
        binding.historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.dashboardButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        quizListAdapter = QuizListAdapter(emptyList())
        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quizListAdapter
        }
    }

    private fun observeViewModel() {

        viewModel.quizList.observe(this) { quizzes ->

            if (quizzes.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.mainRecyclerView.visibility = View.VISIBLE
                quizListAdapter.updateData(quizzes)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->

            if (isLoading && quizListAdapter.itemCount == 0) {
                binding.progressBar.visibility = View.VISIBLE
                binding.mainRecyclerView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.mainRecyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
