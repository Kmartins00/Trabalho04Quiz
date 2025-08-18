package com.example.quizonline.ui.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizonline.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    //Nome da LLM: Gemini , utilizamos como recurso para auxílio na correção de bugs gerados por códigos incorretos.


    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        setupRecyclerView()
        observeViewModel()

        viewModel.loadHistory()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(emptyList())
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.historyItems.observe(this) { items ->
            adapter.updateData(items)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.totalQuizzesText.observe(this) { text ->
            binding.totalQuizzesText.text = text
        }

        viewModel.averageScoreText.observe(this) { text ->
            binding.averageScoreText.text = text
        }
    }
}
