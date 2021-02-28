package com.example.voucherprocessor.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voucherprocessor.databinding.ActivityMainBinding
import com.example.voucherprocessor.models.VoucherCard
import com.example.voucherprocessor.utils.ConstValues


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainActivityView = binding.root
        setContentView(mainActivityView)
        val mockVoucherCard = VoucherCard(10.0, 20.0)

        binding.twoDollarsButton.setOnClickListener {
            if (mockVoucherCard.firstBalance < 2.0) {
                Toast.makeText(
                    this,
                    "The first balance is not enough to proceed this transaction!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                mockVoucherCard.firstBalance -= 2.0
                sendNewBalancesToBeWritten(1, mockVoucherCard)
            }
        }
        binding.fiveDollarsButton.setOnClickListener {
            if (mockVoucherCard.secondBalance < 5.0) {
                Toast.makeText(
                    this,
                    "The second balance is not enough to proceed this transaction!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                mockVoucherCard.secondBalance -= 5.0
                sendNewBalancesToBeWritten(2, mockVoucherCard)
            }
        }
        binding.resetButton.setOnClickListener {
            mockVoucherCard.firstBalance = 10.0
            mockVoucherCard.secondBalance = 20.0
            Toast.makeText(
                this,
                "The balances of the voucher card have been reset!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun sendNewBalancesToBeWritten(balanceNo: Int, newCardData: VoucherCard) {
        val intent = Intent(this, DataWriterActivity::class.java)
        intent.putExtra(ConstValues.BALANCE_NO, balanceNo)
        intent.putExtra(ConstValues.FIRST_BALANCE, newCardData.firstBalance)
        intent.putExtra(ConstValues.SECOND_BALANCE, newCardData.secondBalance)
        startActivity(intent)
    }
}