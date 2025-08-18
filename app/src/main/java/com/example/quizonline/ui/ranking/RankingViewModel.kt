
package com.example.quizonline.ui.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizonline.data.model.UserModel
import com.example.quizonline.data.repository.UserRepository

class RankingViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _rankingList = MutableLiveData<List<UserModel>>()
    val rankingList: LiveData<List<UserModel>> = _rankingList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchRanking() {
        _isLoading.value = true
        repository.fetchRanking(
            onSuccess = { users ->
                _rankingList.value = users
                _isLoading.value = false
            },
            onFailure = { exception ->
                _errorMessage.value = "Erro ao buscar o ranking: ${exception.message}"
                _isLoading.value = false
            }
        )
    }
}
