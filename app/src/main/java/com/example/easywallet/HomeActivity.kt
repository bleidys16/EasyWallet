package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easywallet.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var isBalanceVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupRecyclerView()
    }

    private fun setupUI() {
        updateBalanceVisibility()
        val count = WalletRepository.getNotificationCount()
        if (count > 0) {
            binding.tvNotificationBadge.text = count.toString()
            binding.tvNotificationBadge.visibility = View.VISIBLE
        } else {
            binding.tvNotificationBadge.visibility = View.GONE
            binding.cvNotificationPopup.visibility = View.GONE
        }
    }

    private fun setupListeners() {
        // Notification Button Logic
        binding.btnNotifications.setOnClickListener {
            if (binding.cvNotificationPopup.visibility == View.VISIBLE) {
                binding.cvNotificationPopup.visibility = View.GONE
                WalletRepository.clearNotifications()
                setupUI()
            } else if (WalletRepository.getNotificationCount() > 0) {
                binding.tvNotificationMsg.text = WalletRepository.getMovements().firstOrNull()?.type ?: "Nueva recarga recibida"
                binding.cvNotificationPopup.visibility = View.VISIBLE
            }
        }

        binding.btnShowBalance.setOnClickListener {
            isBalanceVisible = !isBalanceVisible
            updateBalanceVisibility()
        }

        binding.btnCentralTransfer.setOnClickListener {
            startActivity(Intent(this, TransferActivity::class.java))
        }

        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnQuickTransfer.setOnClickListener {
            startActivity(Intent(this, TransferActivity::class.java))
        }

        binding.btnQuickHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnQuickRecharge.setOnClickListener {
            startActivity(Intent(this, RecargaActivity::class.java))
        }
        
        binding.btnMenu.setOnClickListener {
            // Hamburger icon in Home is now decorative as requested
        }
    }

    private fun updateBalanceVisibility() {
        if (isBalanceVisible) {
            binding.tvBalanceValue.text = WalletRepository.getFormattedBalance()
            binding.btnShowBalance.setImageResource(R.drawable.ic_eye_visible)
        } else {
            binding.tvBalanceValue.text = "••••••••"
            binding.btnShowBalance.setImageResource(R.drawable.ic_eye_hidden)
        }
    }

    private fun setupRecyclerView() {
        val movements = WalletRepository.getMovements()
        if (movements.isEmpty()) {
            binding.tvEmptyHistory.visibility = View.VISIBLE
            binding.rvMovements.visibility = View.GONE
        } else {
            binding.tvEmptyHistory.visibility = View.GONE
            binding.rvMovements.visibility = View.VISIBLE
            val limitedMovements = movements.take(3)
            binding.rvMovements.layoutManager = LinearLayoutManager(this)
            binding.rvMovements.adapter = MovementAdapter(limitedMovements)
        }
    }
}