
package com.example.quizonline.ui.ranking

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizonline.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private lateinit var viewModel: RankingViewModel
    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[RankingViewModel::class.java]


        setupRecyclerView()


        observeViewModel()


        viewModel.fetchRanking()
    }

    private fun setupRecyclerView() {
        adapter = RankingAdapter(emptyList())
        binding.rankingRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.rankingRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.rankingList.observe(this) { userList ->
            adapter.updateData(userList)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
