package com.example.voucherprocessor.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voucherprocessor.R
import com.example.voucherprocessor.models.VoucherCard
import com.example.voucherprocessor.utils.ConstValues
import kotlinx.android.synthetic.main.activity_data_writer.*

class DataWriterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_writer)
        // receive the calculated data from the first activity
        val voucherCard = receiveVoucherCardData()
        writeNewVoucherDataToCard(voucherCard)
        addNewTransactionToDB(voucherCard)
        first_balance_text_view.text = voucherCard.firstBalance.toString()
        second_balance_text_view.text = voucherCard.secondBalance.toString()
    }

    private fun receiveVoucherCardData(): VoucherCard {
        val firstBalance = intent.extras?.getDouble(ConstValues.FIRST_BALANCE)!!
        val secondBalance = intent.extras?.getDouble(ConstValues.SECOND_BALANCE)!!
        return VoucherCard(firstBalance, secondBalance)
    }

    private fun writeNewVoucherDataToCard(voucherCard: VoucherCard) {

    }

    private fun addNewTransactionToDB(voucherCard: VoucherCard) {

    }
}