package com.example.voucherprocessor.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// this code was copy-pasted from here : https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
var API_BASE_URL = "https://api.exchangeratesapi.io/"

object RetrofitService {
    var httpClient = OkHttpClient.Builder()

    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create()
        )

    var retrofit: Retrofit = builder
        .client(httpClient.build())
        .build()
    val exchangeRateApi: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
}