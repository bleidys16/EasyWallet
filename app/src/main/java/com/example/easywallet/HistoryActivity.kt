package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easywallet.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupToolbar()
        setupDrawer()
        setupRecyclerView()
    }

    private fun setupUI() {
        binding.tvBalanceValue.text = WalletRepository.getFormattedBalance()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayoutHistory.openDrawer(GravityCompat.START)
        }
    }

    private fun setupDrawer() {
        binding.navViewHistory.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drawer_transfer -> startActivity(Intent(this, TransferActivity::class.java))
                R.id.nav_drawer_recharge -> startActivity(Intent(this, RecargaActivity::class.java))
                R.id.nav_drawer_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
            binding.drawerLayoutHistory.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupRecyclerView() {
        val movements = WalletRepository.getMovements()
        if (movements.isEmpty()) {
            binding.tvEmptyHistory.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvEmptyHistory.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
            binding.rvHistory.layoutManager = LinearLayoutManager(this)
            binding.rvHistory.adapter = MovementAdapter(movements)
        }
    }
}