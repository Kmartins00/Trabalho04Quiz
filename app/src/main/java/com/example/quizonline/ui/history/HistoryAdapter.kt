package com.example.quizonline.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizonline.databinding.HistoryItemRecyclerRowBinding

class HistoryAdapter(private var historyItems: List<HistoryItemModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<HistoryItemModel>) {
        this.historyItems = newItems
        notifyDataSetChanged()
    }

    class HistoryViewHolder(private val binding: HistoryItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItemModel) {
            binding.quizTitleText.text = item.quizTitle
            binding.scoreText.text = item.scoreText
            binding.dateText.text = item.dateText
        }
    }
}
