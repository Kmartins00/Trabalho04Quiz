package com.example.quizonline.ui.quiz

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizonline.R
import com.example.quizonline.data.model.QuizModel
import com.example.quizonline.databinding.ActivityQuizBinding
import com.example.quizonline.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {



    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]


        val quizModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("QUIZ_MODEL", QuizModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("QUIZ_MODEL")
        }

        setupClickListeners()
        observeViewModel()

        quizModel?.let { model ->
            viewModel.startQuiz(model)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.currentQuestion.observe(this) { question ->
            binding.questionTextView.text = question.question
            binding.btn0.text = question.options[0]
            binding.btn1.text = question.options[1]
            binding.btn2.text = question.options[2]
            binding.btn3.text = question.options[3]
            resetButtonColors()
        }

        viewModel.questionProgressText.observe(this) { text ->
            binding.questionIndicatorTextview.text = text
        }

        viewModel.questionProgressValue.observe(this) { progress ->
            binding.questionProgressIndicator.progress = progress
        }

        viewModel.timerText.observe(this) { text ->
            binding.timerIndicatorTextview.text = text
        }

        viewModel.quizFinishedEvent.observe(this) { (score, totalQuestions) ->
            showResultDialog(score, totalQuestions)
        }
    }

    override fun onClick(view: View?) {
        val clickedBtn = view as? Button ?: return

        if (clickedBtn.id == R.id.nextBtn) {
            if (viewModel.selectedAnswer.isEmpty()) {
                Toast.makeText(applicationContext, "Por favor, selecione uma opção", Toast.LENGTH_SHORT).show()
                return
            }
            viewModel.onNextClicked()
        } else {
            viewModel.selectedAnswer = clickedBtn.text.toString()
            resetButtonColors()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun resetButtonColors() {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
    }

    private fun showResultDialog(score: Int, totalQuestions: Int) {
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage >= 60) {
                scoreTitle.text = "Parabéns! Você passou"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Reprovado!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score de um total de $totalQuestions questões estão corretas"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}
