package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easywallet.databinding.ActivityHomeBinding

/**
 * Home Activity
 * Main screen of the application showing balance and actions.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
        setupRecyclerView()
    }

    /**
     * Configures the initial state of the UI components.
     */
    private fun setupUI() {
        // Initially, we show the empty state if there are no movements
        binding.tvEmptyHistory.visibility = View.VISIBLE
        binding.rvMovements.visibility = View.GONE
    }

    /**
     * Sets up click listeners for buttons and navigation items.
     */
    private fun setupListeners() {
        // Navigate to TransferActivity via CardButton
        binding.btnTransfer.setOnClickListener {
            startActivity(Intent(this, TransferActivity::class.java))
        }

        // Navigate to HistoryActivity via CardButton
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        
        // Setup Bottom Navigation View interactions
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_transfer -> {
                    startActivity(Intent(this, TransferActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> true
            }
        }
    }

    /**
     * Initializes the RecyclerView with an empty list as per project requirements.
     */
    private fun setupRecyclerView() {
        // Initialize with empty list as per requirements
        val movements = emptyList<Movement>()
        binding.rvMovements.layoutManager = LinearLayoutManager(this)
        binding.rvMovements.adapter = MovementAdapter(movements)
    }
}