package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.repository.AuthRepository

class SettingsViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _passwordChangeResult = MutableLiveData<Boolean>()
    val passwordChangeResult: LiveData<Boolean>
        get() = _passwordChangeResult

    fun changePassword(newPassword: String) {
        authRepository.changePassword(newPassword) { success ->
            _passwordChangeResult.postValue(success)
        }
    }
}
