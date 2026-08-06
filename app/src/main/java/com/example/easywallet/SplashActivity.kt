package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easywallet.databinding.ActivitySplashBinding

/**
 * Splash Screen Activity
 * Displays the logo and app name with a fade-in animation.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force Light Mode always
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fix for Status Bar overlapping
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load and start fade-in animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.ivLogo.startAnimation(fadeIn)
        binding.tvAppName.startAnimation(fadeIn)
        binding.tvSlogan.startAnimation(fadeIn)
        binding.ivSecurity.startAnimation(fadeIn)

        // Navigate to HomeActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}