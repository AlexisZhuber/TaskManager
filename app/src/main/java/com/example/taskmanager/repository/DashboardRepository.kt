package com.example.taskmanager.repository

import com.example.taskmanager.network.RetrofitInstance

class DashboardRepository {
    private val apiKey = "901145adea1fd6983e9254c39c1a54ac"

    suspend fun getWeather(city: String) = RetrofitInstance.api.getWeather(city, apiKey)
}
