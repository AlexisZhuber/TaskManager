package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.network.BitcoinPriceResponse
import com.example.taskmanager.repository.DashboardRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class DashboardViewModel : ViewModel() {

    private val _completionRate = MutableLiveData<Float>()
    val completionRate: LiveData<Float> get() = _completionRate

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> get() = _totalTasks

    private val _completedTasks = MutableLiveData<Int>()
    val completedTasks: LiveData<Int> get() = _completedTasks

    private val _averageTaskDuration = MutableLiveData<Float>()
    val averageTaskDuration: LiveData<Float> get() = _averageTaskDuration

    private val _taskDurations = MutableLiveData<List<Pair<String, Float>>>()
    val taskDurations: LiveData<List<Pair<String, Float>>> get() = _taskDurations

    private val repository = DashboardRepository()
    val bitcoinPrice = MutableLiveData<BitcoinPriceResponse>()

    fun loadDashboardData() {
        // Simulate data
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul")
        val simulatedData = months.map { month ->
            month to Random.nextInt(0, 150).toFloat()
        }

        _completionRate.value = 75f  // Simulated completion rate
        _totalTasks.value = 200  // Simulated total tasks
        _completedTasks.value = 150  // Simulated completed tasks
        _averageTaskDuration.value = 2.5f  // Simulated average task duration
        _taskDurations.value = simulatedData
    }

    fun fetchBitcoinPrice() {
        viewModelScope.launch {
            try {
                val fetchedPrice = repository.getBitcoinPrice()
                bitcoinPrice.value = fetchedPrice
            } catch (e: Exception) {
                // Manejo de errores
            }
        }
    }
}
