package com.example.quizonline.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizonline.data.local.AppDatabase
import com.example.quizonline.data.model.QuizModel
import com.example.quizonline.data.repository.QuizRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository

    val quizList: LiveData<List<QuizModel>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        val quizDao = AppDatabase.getDatabase(application).quizDao()
        repository = QuizRepository(quizDao)


        quizList = repository.getQuizzesFlow().asLiveData()


        loadQuizzes()
    }

    private fun loadQuizzes() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.refreshQuizzes()
            } catch (e: Exception) {

                if (quizList.value.isNullOrEmpty()) {
                    _errorMessage.postValue("Erro ao carregar quizzes: ${e.message}")
                }
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
