package com.example.voucherprocessor.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voucherprocessor.databinding.ActivityDataWriterBinding
import com.example.voucherprocessor.db.Transaction
import com.example.voucherprocessor.models.VoucherCard
import com.example.voucherprocessor.services.DatabaseService
import com.example.voucherprocessor.services.RetrofitService
import com.example.voucherprocessor.utils.ConstValues
import com.example.voucherprocessor.utils.UtilFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataWriterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataWriterBinding
    private var balanceNo: Int = 0
    private lateinit var voucherCard: VoucherCard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataWriterBinding.inflate(layoutInflater)
        val dataWriterActivityView = binding.root
        setContentView(dataWriterActivityView)
        // receive the calculated data from the first activity
        voucherCard = receiveVoucherCardData()
        writeNewVoucherDataToCard()
        binding.firstBalanceTextView.text = voucherCard.firstBalance.toString()
        binding.secondBalanceTextView.text = voucherCard.secondBalance.toString()
        getExchangeRate()
    }

    private fun receiveVoucherCardData(): VoucherCard {
        balanceNo = intent.extras?.getInt(ConstValues.BALANCE_NO)!!
        val firstBalance = intent.extras?.getDouble(ConstValues.FIRST_BALANCE)!!
        val secondBalance = intent.extras?.getDouble(ConstValues.SECOND_BALANCE)!!
        return VoucherCard(firstBalance, secondBalance)
    }

    private fun writeNewVoucherDataToCard() {

    }

    private suspend fun addNewTransactionToDB(usdExchangeRate: Double) {
        val newTransaction: Transaction
        when (balanceNo) {
            1 -> {
                newTransaction = Transaction(
                    0,
                    balanceNo,
                    voucherCard.firstBalance + 2.0,
                    voucherCard.firstBalance,
                    UtilFunctions.roundDouble(voucherCard.firstBalance / usdExchangeRate),
                    usdExchangeRate
                )
                DatabaseService.transactionsDao.insertTransaction(newTransaction)
            }
            2 -> {
                newTransaction = Transaction(
                    0,
                    balanceNo,
                    voucherCard.secondBalance + 5.0,
                    voucherCard.secondBalance,
                    UtilFunctions.roundDouble(voucherCard.secondBalance / usdExchangeRate),
                    usdExchangeRate
                )
                try {
                    DatabaseService.transactionsDao.insertTransaction(newTransaction)
                } catch (e: Exception) {
                    e.localizedMessage
                }
            }
        }
    }

    private fun getExchangeRate() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.exchangeRateApi.getEuroExchangeRate()
            val usdExchangeRate = response.rates.usd
            addNewTransactionToDB(usdExchangeRate)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@DataWriterActivity,
                    "the transaction has been inserted successfully!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}