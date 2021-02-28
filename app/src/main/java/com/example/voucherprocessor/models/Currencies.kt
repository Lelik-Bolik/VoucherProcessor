package com.example.voucherprocessor.models

import com.google.gson.annotations.SerializedName

data class Currencies(
    @SerializedName("USD")
    val usd : Double
)