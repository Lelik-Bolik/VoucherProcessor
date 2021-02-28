package com.example.voucherprocessor.activities

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
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
import java.nio.charset.Charset
import java.util.*

class DataWriterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataWriterBinding
    private var balanceNo: Int = 0
    private lateinit var voucherCard: VoucherCard
    private var nfcAdapter: NfcAdapter? = null
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
        // get the exchange rate and add a new transaction record to the DB
        getExchangeRate()
    }

    private fun receiveVoucherCardData(): VoucherCard {
        balanceNo = intent.extras?.getInt(ConstValues.BALANCE_NO)!!
        val firstBalance = intent.extras?.getDouble(ConstValues.FIRST_BALANCE)!!
        val secondBalance = intent.extras?.getDouble(ConstValues.SECOND_BALANCE)!!
        return VoucherCard(firstBalance, secondBalance)
    }

    private fun writeNewVoucherDataToCard() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val firstRecords = createTextRecord(voucherCard.firstBalance.toString(), Locale("en"), true)
        val secondRecords =
            createTextRecord(voucherCard.secondBalance.toString(), Locale("en"), true)
        val records = arrayOf(firstRecords, secondRecords)
        val dataToWrite = NdefMessage(records)
        // the following tag reference will be null and a crash will happen, so comment out the following line
        //  val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        //  val ndef = Ndef.get(tag)
        //  ndef.connect()
        //  ndef.writeNdefMessage(dataToWrite)
        //  ndef.close()
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

    private suspend fun addNewTransactionToDB(usdExchangeRate: Double) {
        val newTransaction: Transaction
        // check which balance has been reduced
        when (balanceNo) {
            ConstValues.FIRST_BALANCE_NUM -> {
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
            ConstValues.SECOND_BALANCE_NUM -> {
                newTransaction = Transaction(
                    0,
                    balanceNo,
                    voucherCard.secondBalance + 5.0,
                    voucherCard.secondBalance,
                    UtilFunctions.roundDouble(voucherCard.secondBalance / usdExchangeRate),
                    usdExchangeRate
                )
                DatabaseService.transactionsDao.insertTransaction(newTransaction)
            }
        }
    }

    // the following funciton was copy-pasted from here :https://developer.android.com/guide/topics/connectivity/nfc/nfc#well-known-text
    private fun createTextRecord(
        payload: String,
        locale: Locale,
        encodeInUtf8: Boolean
    ): NdefRecord {
        val langBytes = locale.language.toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes = payload.toByteArray(utfEncoding)
        val utfBit: Int = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }
}