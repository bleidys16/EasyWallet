package com.example.easywallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easywallet.databinding.ActivityTransferBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Transfer Activity
 * Screen to simulate money transfers with validations.
 */
class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListeners() {
        binding.btnSendTransfer.setOnClickListener {
            performTransfer()
        }
    }

    private fun performTransfer() {
        val recipient = binding.etRecipient.text.toString().trim()
        val amountStr = binding.etAmount.text.toString().trim()

        // Validation 1: Check for empty fields
        if (recipient.isEmpty() || amountStr.isEmpty()) {
            showSnackbar(getString(R.string.error_empty_fields))
            return
        }

        // Validation 2: Check for amount > 0
        val amount = amountStr.toDoubleOrNull() ?: 0.0
        if (amount <= 0) {
            showSnackbar(getString(R.string.error_invalid_amount))
            return
        }

        // Success: Show Snackbar and return to Home
        Snackbar.make(binding.root, R.string.transfer_success, Snackbar.LENGTH_LONG)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    finish() // Return to Home
                }
            }).show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}