
package com.example.quizonline.ui.ranking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizonline.data.model.UserModel
import com.example.quizonline.databinding.RankingItemRowBinding

class RankingAdapter(private var userList: List<UserModel>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = RankingItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(userList[position], position + 1)
    }

    override fun getItemCount(): Int = userList.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUserList: List<UserModel>) {
        this.userList = newUserList
        notifyDataSetChanged()
    }

    class RankingViewHolder(private val binding: RankingItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel, rank: Int) {
            binding.rankText.text = "$rank."
            binding.userNameText.text = user.name
            binding.userScoreText.text = "${user.totalScore} pts"
        }
    }
}
