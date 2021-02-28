package com.example.voucherprocessor.activities

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voucherprocessor.databinding.ActivityMainBinding
import com.example.voucherprocessor.models.VoucherCard
import com.example.voucherprocessor.utils.ConstValues
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var voucherCard: VoucherCard
    private var initialFirstBalance  : Double = 0.0
    private var initialSecondBalance : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainActivityView = binding.root
        setContentView(mainActivityView)
        // since I don't have an NFC tag to read data from, initialize voucherCard with mock data
        voucherCard = VoucherCard(10.0, 20.0)

        binding.twoDollarsButton.setOnClickListener {
            // check if the voucherCard has been initialized (i.e. the voucher has been detected and read)
            if (::voucherCard.isInitialized) {
                // check if the balance is enough
                if (voucherCard.firstBalance < 2.0) {
                    Toast.makeText(
                        this,
                        "The first balance is not enough to proceed this transaction!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    voucherCard.firstBalance -= 2.0
                    sendNewBalancesToBeWritten(ConstValues.FIRST_BALANCE_NUM, voucherCard)
                }
            } else {
                // the voucherCard hasn't been initialized so do nothing
                // toast can be shown here
                return@setOnClickListener
            }

        }
        binding.fiveDollarsButton.setOnClickListener {
            // check if the voucherCard has been initialized (i.e. the voucher has been detected and read)
            if (::voucherCard.isInitialized) {
                // check if the balance is enough
                if (voucherCard.secondBalance < 5.0) {
                    Toast.makeText(
                        this,
                        "The second balance is not enough to proceed this transaction!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    voucherCard.secondBalance -= 5.0
                    sendNewBalancesToBeWritten(ConstValues.SECOND_BALANCE_NUM, voucherCard)
                }
            } else {
                // the voucherCard hasn't been initialized so do nothing
                // toast can be shown here
                return@setOnClickListener
            }

        }
        binding.resetButton.setOnClickListener {
            if (::voucherCard.isInitialized) {
                // reset the balances
                voucherCard.firstBalance = initialFirstBalance
                voucherCard.secondBalance = initialSecondBalance
                Toast.makeText(
                    this,
                    "The balances of the voucher card have been reset!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            // (let's assume that the data is of the card is written as NDEF messages
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // assuming that there is two message that has the two balances
                val firstPayload = messages[0].records[0].payload
                val secondPayload = messages[1].records[0].payload
                initialFirstBalance = ByteBuffer.wrap(firstPayload).double
                initialSecondBalance = ByteBuffer.wrap(secondPayload).double
                // initialize voucherCard with the data read from the voucher card
                voucherCard = VoucherCard(initialFirstBalance, initialSecondBalance)
                Toast.makeText(
                    this,
                    "voucher card detected and read successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
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