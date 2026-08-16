package com.example.easywallet

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnIngresar.setOnClickListener {
            val correo = binding.etCorreo.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                correo.isEmpty() -> Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                else -> validarLogin(correo, password)
            }
        }

        binding.tvRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        setupPasswordVisibility()

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPasswordVisibility() {
        var isPasswordVisible = false
        binding.ivShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivShowPassword.setImageResource(R.drawable.ic_eye_hidden)
            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivShowPassword.setImageResource(R.drawable.ic_eye_visible)
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }

    private fun validarLogin(correo: String, password: String) {
        // Se utiliza Coroutine (lifecycleScope) para no bloquear el hilo principal (UI)
        // al realizar operaciones de base de datos que pueden ser lentas.
        lifecycleScope.launch {
            val usuario = database.usuarioDao().validarLogin(correo, password)
            if (usuario != null) {
                Toast.makeText(this@LoginActivity, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("USUARIO_NOMBRE", usuario.nombre)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
