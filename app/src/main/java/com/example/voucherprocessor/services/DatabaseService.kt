package com.example.voucherprocessor.services

import androidx.room.Room
import com.example.voucherprocessor.App
import com.example.voucherprocessor.db.AppDatabase

object DatabaseService {
    private val db = Room.databaseBuilder(
        App.appContext,
        AppDatabase::class.java, "vouchers-database"
    ).build()

    val transactionsDao = db.transactionDao()
}