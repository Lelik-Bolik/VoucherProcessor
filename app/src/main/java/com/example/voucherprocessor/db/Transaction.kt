package com.example.voucherprocessor.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionNo: Int,
    @ColumnInfo(name = "BalanceNo") val balanceNo: Int,
    @ColumnInfo(name = "OldBalance_USD") val oldBalanceUsd: Double,
    @ColumnInfo(name = "NewBalance_USD") val newBalanceUsd: Double,
    @ColumnInfo(name = "NewBalance_EUR") val newBalanceEur: Double,
    @ColumnInfo(name = "ExchangeRate") val exchangeRate: Double
)