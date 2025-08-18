// Local: ui/main/QuizListAdapter.kt
package com.example.quizonline.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizonline.ui.quiz.QuizActivity
import com.example.quizonline.data.model.QuizModel
import com.example.quizonline.databinding.QuizItemRecyclerRowBinding

//Nome da LLM: Gemini , utilizamos como recurso para auxílio na correção de bugs gerados por códigos incorretos.


class QuizListAdapter(private var quizModelList : List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model : QuizModel){
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " min"
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)

                    intent.putExtra("QUIZ_MODEL", model) // 'model' é o QuizModel que o método bind recebe

                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newQuizList: List<QuizModel>) {
        this.quizModelList = newQuizList
        notifyDataSetChanged()
    }
}
