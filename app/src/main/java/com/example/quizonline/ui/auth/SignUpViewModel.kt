
package com.example.quizonline.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizonline.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun signUpUser(name: String, email: String, password: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Por favor, preencha todos os campos."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.signUp(name, email, password)
                _signUpSuccess.postValue(true)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "Ocorreu um erro desconhecido.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
