package com.example.voucherprocessor.services

import com.example.voucherprocessor.models.ExchangeRateApiResponse
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getEuroExchangeRate() :ExchangeRateApiResponse
}