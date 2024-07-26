package com.example.taskmanager.network

import retrofit2.http.GET

interface ApiService {
    @GET("v1/bpi/currentprice.json")
    suspend fun getBitcoinPrice(): BitcoinPriceResponse
}

data class BitcoinPriceResponse(
    val time: Time,
    val bpi: Bpi
)

data class Time(
    val updated: String,
    val updatedISO: String,
    val updateduk: String
)

data class Bpi(
    val USD: Currency,
    val GBP: Currency,
    val EUR: Currency
)

data class Currency(
    val code: String,
    val rate: String,
    val description: String,
    val rate_float: Float
)
