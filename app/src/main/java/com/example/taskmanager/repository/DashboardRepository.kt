package com.example.taskmanager.repository

import com.example.taskmanager.network.RetrofitInstance

class DashboardRepository {
    suspend fun getBitcoinPrice() = RetrofitInstance.api.getBitcoinPrice()
}
