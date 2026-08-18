package com.example.easywallet

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.database.Transaccion
import com.example.easywallet.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: AppDatabase
    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        usuarioId = intent.getIntExtra("USUARIO_ID", -1)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val saldo = database.transaccionDao().obtenerSaldoPorUsuario(usuarioId) ?: 0.0
            binding.tvBalanceValue.text = formatCurrency(saldo)

            val transacciones = database.transaccionDao().obtenerTransaccionesPorUsuario(usuarioId)
            setupRecyclerView(transacciones)
        }
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("$", "$ ").replace(",", ".")
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun setupRecyclerView(transacciones: List<Transaccion>) {
        if (transacciones.isEmpty()) {
            binding.tvEmptyHistory.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvEmptyHistory.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
            
            val movements = transacciones.map { t ->
                Movement(
                    type = t.nombre,
                    date = t.fecha,
                    amount = (if (t.tipo == "INGRESO") "+ " else "- ") + formatCurrency(t.monto),
                    status = "Completado",
                    isPositive = t.tipo == "INGRESO",
                    iconRes = t.iconoResId ?: R.drawable.ic_transfer_modern
                )
            }
            
            binding.rvHistory.layoutManager = LinearLayoutManager(this)
            binding.rvHistory.adapter = MovementAdapter(movements)
        }
    }
}
