package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.repository.AuthRepository

class RegisterViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _registerResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult: LiveData<Pair<Boolean, String?>>
        get() = _registerResult

    // Function to initiate the registration process and update the registration result
    fun register(email: String, password: String) {
        authRepository.register(email, password) { success, message ->
            _registerResult.postValue(Pair(success, message))
        }
    }
}
