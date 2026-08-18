package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.database.Transaccion
import com.example.easywallet.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: AppDatabase
    private var isBalanceVisible = true
    private var usuarioId: Int = -1
    private var currentBalance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        usuarioId = intent.getIntExtra("USUARIO_ID", -1)

        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupDrawer()
    }

    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }

    private fun loadDataFromDatabase() {
        val nombreUsuario = intent.getStringExtra("USUARIO_NOMBRE") ?: "Usuario"
        binding.tvGreeting.text = "Hola, $nombreUsuario"

        lifecycleScope.launch {
            // Obtener Saldo real desde SQLite
            val saldo = database.transaccionDao().obtenerSaldoPorUsuario(usuarioId) ?: 0.0
            currentBalance = saldo
            updateBalanceUI()

            // Obtener Transacciones reales desde SQLite
            val transacciones = database.transaccionDao().obtenerTransaccionesPorUsuario(usuarioId)
            setupRecyclerView(transacciones)

            // Notificaciones (basadas en la última transacción si es ingreso)
            val ultima = transacciones.firstOrNull()
            if (ultima != null && ultima.tipo == "INGRESO") {
                binding.tvNotificationBadge.text = "1"
                binding.tvNotificationBadge.visibility = View.VISIBLE
                binding.tvNotificationMsg.text = "Recibiste ${formatCurrency(ultima.monto)}"
            } else {
                binding.tvNotificationBadge.visibility = View.GONE
                binding.cvNotificationPopup.visibility = View.GONE
            }
        }
    }

    private fun setupDrawer() {
        val headerView = binding.navigationView.getHeaderView(0)
        val tvHeaderName = headerView.findViewById<TextView>(R.id.tv_header_name)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.tv_header_email)

        val nombre = intent.getStringExtra("USUARIO_NOMBRE") ?: "Usuario"
        val correo = intent.getStringExtra("USUARIO_CORREO") ?: "usuario@correo.com"

        tvHeaderName.text = nombre
        tvHeaderEmail.text = correo

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_logout) {
                cerrarSesion()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun cerrarSesion() {
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupListeners() {
        binding.ivMenuDecorative.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnNotifications.setOnClickListener {
            if (binding.cvNotificationPopup.visibility == View.VISIBLE) {
                binding.cvNotificationPopup.visibility = View.GONE
            } else if (binding.tvNotificationBadge.visibility == View.VISIBLE) {
                binding.cvNotificationPopup.visibility = View.VISIBLE
                binding.tvNotificationBadge.visibility = View.GONE
            }
        }

        binding.btnShowBalance.setOnClickListener {
            isBalanceVisible = !isBalanceVisible
            updateBalanceUI()
        }

        binding.btnCentralTransfer.setOnClickListener {
            val intent = Intent(this, TransferActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.navHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.btnQuickTransfer.setOnClickListener {
            val intent = Intent(this, TransferActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.btnQuickHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.btnQuickRecharge.setOnClickListener {
            val intent = Intent(this, RecargaActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.tvViewAll.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun updateBalanceUI() {
        if (isBalanceVisible) {
            binding.tvBalanceValue.text = formatCurrency(currentBalance)
            binding.btnShowBalance.setImageResource(R.drawable.ic_eye_visible)
        } else {
            binding.tvBalanceValue.text = "••••••••"
            binding.btnShowBalance.setImageResource(R.drawable.ic_eye_hidden)
        }
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("$", "$ ").replace(",", ".")
    }

    private fun setupRecyclerView(transacciones: List<Transaccion>) {
        if (transacciones.isEmpty()) {
            binding.tvEmptyHistory.visibility = View.VISIBLE
            binding.rvMovements.visibility = View.GONE
        } else {
            binding.tvEmptyHistory.visibility = View.GONE
            binding.rvMovements.visibility = View.VISIBLE
            
            val movements = transacciones.take(3).map { t ->
                Movement(
                    type = t.nombre,
                    date = t.fecha,
                    amount = (if (t.tipo == "INGRESO") "+ " else "- ") + formatCurrency(t.monto),
                    status = "Completado",
                    isPositive = t.tipo == "INGRESO",
                    iconRes = t.iconoResId ?: R.drawable.ic_transfer_modern
                )
            }
            
            binding.rvMovements.layoutManager = LinearLayoutManager(this)
            binding.rvMovements.adapter = MovementAdapter(movements)
        }
    }
}
