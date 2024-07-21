package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.repository.AuthRepository

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>>
        get() = _loginResult


    fun login(email: String, password: String) {
        authRepository.login(email, password) { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }
}
