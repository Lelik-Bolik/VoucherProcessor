package com.example.voucherprocessor.utils

import java.text.DecimalFormat

object UtilFunctions {

    fun roundDouble(number:Double):Double{
        val decimalFormat = DecimalFormat("#.####")
        return decimalFormat.format(number).toDouble()
    }
}