// Local: ui/dashboard/DashboardViewModel.kt
package com.example.quizonline.ui.dashboard

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quizonline.data.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
//Nome da LLM: Gemini , utilizamos como recurso para o auxílio na programação da coleta de dados.

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()

    private val _welcomeText = MutableLiveData<String>()
    val welcomeText: LiveData<String> = _welcomeText

    private val _totalScoreText = MutableLiveData<String>()
    val totalScoreText: LiveData<String> = _totalScoreText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> = _navigateToLogin

    init {
        // A busca de dados começa aqui e fica atualizando.
        observeUserData()
    }

    private fun observeUserData() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.observeCurrentUser().collect { user ->
                _isLoading.postValue(false) // Interrompe o loading após a primeira emissão
                if (user != null) {
                    _welcomeText.postValue("Bem-vindo, ${user.name}!")
                    _totalScoreText.postValue("${user.totalScore} pts")
                } else {
                    _welcomeText.postValue("Bem-vindo!")
                    _totalScoreText.postValue("0 pts")
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        val sharedPreferences = getApplication<Application>().getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        _navigateToLogin.value = true
    }
}
