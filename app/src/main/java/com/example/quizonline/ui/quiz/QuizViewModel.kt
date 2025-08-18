
package com.example.quizonline.ui.quiz

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizonline.data.local.AppDatabase
import com.example.quizonline.data.model.QuestionModel
import com.example.quizonline.data.model.QuizModel
import com.example.quizonline.data.model.QuizResultModel
import com.example.quizonline.data.repository.HistoryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository
    private lateinit var questionList: List<QuestionModel>
    private var timer: CountDownTimer? = null

    private var currentQuestionIndex = 0
    private var score = 0
    var selectedAnswer = ""
    private var quizId: String = ""

    private val _currentQuestion = MutableLiveData<QuestionModel>()
    val currentQuestion: LiveData<QuestionModel> = _currentQuestion

    private val _questionProgressText = MutableLiveData<String>()
    val questionProgressText: LiveData<String> = _questionProgressText

    private val _questionProgressValue = MutableLiveData<Int>()
    val questionProgressValue: LiveData<Int> = _questionProgressValue

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _quizFinishedEvent = MutableLiveData<Pair<Int, Int>>()
    val quizFinishedEvent: LiveData<Pair<Int, Int>> = _quizFinishedEvent

    init {
        val historyDao = AppDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDao)
    }

    fun startQuiz(quiz: QuizModel) {
        this.quizId = quiz.id
        this.questionList = quiz.questionList
        loadQuestion()
        startTimer(quiz.time)
    }

    fun onNextClicked() {
        if (selectedAnswer.isNotEmpty()) {
            if (selectedAnswer == questionList[currentQuestionIndex].correct) {
                score++
            }
            currentQuestionIndex++
            loadQuestion()
        }
    }

    private fun loadQuestion() {
        selectedAnswer = ""
        if (currentQuestionIndex == questionList.size) {
            finishQuiz()
            return
        }
        _currentQuestion.value = questionList[currentQuestionIndex]
        _questionProgressText.value = "Questão ${currentQuestionIndex + 1}/ ${questionList.size}"
        _questionProgressValue.value = ((currentQuestionIndex.toFloat() / questionList.size.toFloat()) * 100).toInt()
    }

    private fun finishQuiz() {
        timer?.cancel()
        val totalQuestions = questionList.size
        _quizFinishedEvent.value = Pair(score, totalQuestions)
        saveResult(totalQuestions)
    }

    private fun saveResult(totalQuestions: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val result = QuizResultModel(
            id = "",
            quizId = this.quizId,
            userId = userId,
            score = score,
            totalQuestions = totalQuestions,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.saveQuizResult(result)
        }
    }

    private fun startTimer(timeInMinutes: String) {
        val totalTimeInMillis = timeInMinutes.toLongOrNull()?.let { it * 60 * 1000L } ?: 0L
        timer = object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                _timerText.value = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
