package com.example.quizonline.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizonline.data.local.AppDatabase
import com.example.quizonline.data.repository.HistoryRepository
import com.example.quizonline.data.repository.QuizRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val historyRepository: HistoryRepository
    private val quizRepository: QuizRepository

    init {
        val appDatabase = AppDatabase.getDatabase(application)
        val quizDao = appDatabase.quizDao()
        val historyDao = appDatabase.historyDao()
        quizRepository = QuizRepository(quizDao)
        historyRepository = HistoryRepository(historyDao)
    }

    private val _historyItems = MutableLiveData<List<HistoryItemModel>>()
    val historyItems: LiveData<List<HistoryItemModel>> = _historyItems

    private val _totalQuizzesText = MutableLiveData<String>()
    val totalQuizzesText: LiveData<String> = _totalQuizzesText

    private val _averageScoreText = MutableLiveData<String>()
    val averageScoreText: LiveData<String> = _averageScoreText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadHistory() {
        _isLoading.value = true
        viewModelScope.launch {

            val quizzes = quizRepository.getQuizzesOnce()
            val quizTitleMap = quizzes.associateBy({ it.id }, { it.title })

            val results = historyRepository.fetchHistory()

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedItems = results.map { result ->
                HistoryItemModel(
                    quizTitle = quizTitleMap[result.quizId] ?: "Quiz Desconhecido",
                    scoreText = "Pontuação: ${result.score}/${result.totalQuestions}",
                    dateText = "Data: ${sdf.format(Date(result.timestamp))}"
                )
            }
            _historyItems.postValue(formattedItems)

            calculateStats(results)
            _isLoading.postValue(false)
        }
    }

    private fun calculateStats(results: List<com.example.quizonline.data.model.QuizResultModel>) {
        val totalQuizzes = results.size
        val totalScore = results.sumOf { it.score }
        val totalPossibleScore = results.sumOf { it.totalQuestions }
        val averagePercentage = if (totalPossibleScore > 0) {
            (totalScore.toDouble() / totalPossibleScore.toDouble() * 100).toInt()
        } else { 0 }

        _totalQuizzesText.postValue("Total de Quizzes: $totalQuizzes")
        _averageScoreText.postValue("Média de Acertos: $averagePercentage%")
    }
}
