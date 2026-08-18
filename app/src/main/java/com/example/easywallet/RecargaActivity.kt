package com.example.easywallet

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.database.Transaccion
import com.example.easywallet.databinding.ActivityRecargaBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RecargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecargaBinding
    private lateinit var database: AppDatabase
    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecargaBinding.inflate(layoutInflater)
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

        val date = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale("es", "CO")).format(Date())
        
        lifecycleScope.launch {
            val transaccion = Transaccion(
                usuarioId = usuarioId,
                nombre = "Recarga desde $bank",
                monto = amount,
                tipo = "INGRESO",
                fecha = date,
                iconoResId = R.drawable.ic_arrow_incoming
            )
            
            database.transaccionDao().insertarTransaccion(transaccion)

            Snackbar.make(binding.root, R.string.recharge_success, 1500)
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
