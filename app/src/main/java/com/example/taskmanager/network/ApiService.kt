package com.example.taskmanager.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeather(@Query("q") city: String, @Query("appid") apiKey: String): WeatherResponse
}

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float,  // Kelvin
    val pressure: Float,
    val humidity: Float
)
