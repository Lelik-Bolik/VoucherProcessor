package com.example.voucherprocessor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.voucherprocessor.R
import com.example.voucherprocessor.models.VoucherCard
import com.example.voucherprocessor.utils.ConstValues
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mockVoucherCard = VoucherCard(10.0, 20.0)

        two_dollars_button.setOnClickListener {
            if (mockVoucherCard.firstBalance < 2.0) {
                Toast.makeText(this, "The first balance is not enough to proceed this transaction!", Toast.LENGTH_LONG).show()
            } else {
                mockVoucherCard.firstBalance -= 2.0
                sendNewBalancesToBeWritten(mockVoucherCard)
            }
        }
        five_dollars_button.setOnClickListener {
            if (mockVoucherCard.secondBalance < 5.0) {
                Toast.makeText(this, "The second balance is not enough to proceed this transaction!", Toast.LENGTH_LONG).show()
            } else {
                mockVoucherCard.secondBalance -= 5.0
                sendNewBalancesToBeWritten(mockVoucherCard)
            }
        }
        reset_button.setOnClickListener {
            mockVoucherCard.firstBalance = 10.0
            mockVoucherCard.secondBalance = 20.0
            Toast.makeText(this, "The balances of the voucher card have been reset!",Toast.LENGTH_LONG).show()
        }
    }

    private fun sendNewBalancesToBeWritten(newCardData: VoucherCard) {
        val intent = Intent(this, DataWriterActivity::class.java)
        intent.putExtra(ConstValues.FIRST_BALANCE, newCardData.firstBalance)
        intent.putExtra(ConstValues.SECOND_BALANCE, newCardData.secondBalance)
        startActivity(intent)
    }
}