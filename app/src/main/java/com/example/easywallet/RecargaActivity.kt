package com.example.easywallet

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easywallet.databinding.ActivityRecargaBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class RecargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecargaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupDropdown()
        setupListeners()
        
        // Currency formatting
        binding.etAmount.addTextChangedListener(CurrencyTextWatcher(binding.etAmount))
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupDropdown() {
        val banks = resources.getStringArray(R.array.banks_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, banks)
        binding.actBank.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnConfirmRecharge.setOnClickListener {
            performRecharge()
        }
    }

    private fun performRecharge() {
        val bank = binding.actBank.text.toString().trim()
        val amountStr = binding.etAmount.text.toString().trim().replace(".", "")

        if (bank.isEmpty() || amountStr.isEmpty()) {
            Snackbar.make(binding.root, R.string.error_empty_fields, Snackbar.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0
        if (amount < 1000) {
            Snackbar.make(binding.root, R.string.min_transfer_msg, Snackbar.LENGTH_SHORT).show()
            return
        }

        // Create Movement
        val date = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale("es", "CO")).format(Date())
        val movement = Movement(
            type = "Recarga desde $bank",
            date = date,
            amount = "+ " + WalletRepository.getFormattedBalance(amount),
            status = "Completado",
            isPositive = true,
            iconRes = R.drawable.ic_wallet
        )

        WalletRepository.addMovement(movement, amount)

        Snackbar.make(binding.root, R.string.recharge_success, Snackbar.LENGTH_LONG)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    finish()
                }
            }).show()
    }
}