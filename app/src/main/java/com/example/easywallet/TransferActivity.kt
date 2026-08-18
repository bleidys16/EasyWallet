package com.example.easywallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.database.Transaccion
import com.example.easywallet.databinding.ActivityTransferBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding
    private lateinit var database: AppDatabase
    private var usuarioId: Int = -1
    private var currentBalance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        usuarioId = intent.getIntExtra("USUARIO_ID", -1)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupDropdown()
        setupListeners()
        
        binding.etAmount.addTextChangedListener(CurrencyTextWatcher(binding.etAmount))
        loadBalance()
        setupBalanceValidation()
    }

    private fun loadBalance() {
        lifecycleScope.launch {
            currentBalance = database.transaccionDao().obtenerSaldoPorUsuario(usuarioId) ?: 0.0
        }
    }

    private fun setupBalanceValidation() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val amountStr = s.toString().replace(".", "").trim()
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                
                if (amount > 0 && amount > currentBalance) {
                    binding.tilAmount.error = "Saldo insuficiente"
                    binding.tilAmount.isErrorEnabled = true
                } else {
                    binding.tilAmount.error = null
                    binding.tilAmount.isErrorEnabled = false
                }
            }
        })
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
        binding.btnSendTransfer.setOnClickListener {
            performTransfer()
        }
    }

    private fun performTransfer() {
        val bank = binding.actBank.text.toString().trim()
        val account = binding.etAccount.text.toString().trim()
        val recipient = binding.etRecipient.text.toString().trim()
        val amountStr = binding.etAmount.text.toString().trim().replace(".", "")

        if (bank.isEmpty() || account.isEmpty() || recipient.isEmpty() || amountStr.isEmpty()) {
            Snackbar.make(binding.root, R.string.error_empty_fields, Snackbar.LENGTH_SHORT).show()
            return
        }

        if (account.length < 10) {
            binding.tilAccount.error = "Número de cuenta inválido (mínimo 10 dígitos)"
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0
        if (amount < 1000) {
            Snackbar.make(binding.root, R.string.min_transfer_msg, Snackbar.LENGTH_SHORT).show()
            return
        }

        if (amount > currentBalance) {
            binding.tilAmount.error = "Saldo insuficiente"
            return
        }

        val date = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale("es", "CO")).format(Date())
        
        lifecycleScope.launch {
            val transaccion = Transaccion(
                usuarioId = usuarioId,
                nombre = "Transferencia a $recipient",
                monto = amount,
                tipo = "EGRESO",
                fecha = date,
                iconoResId = R.drawable.ic_transfer_modern
            )
            
            database.transaccionDao().insertarTransaccion(transaccion)

            Snackbar.make(binding.root, R.string.transfer_success, 1500)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }).show()
        }
    }
}
