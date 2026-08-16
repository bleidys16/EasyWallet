package com.example.easywallet

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.easywallet.database.AppDatabase
import com.example.easywallet.database.Usuario
import com.example.easywallet.databinding.ActivityRegistroBinding
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        binding.tvVolverLogin.setOnClickListener {
            finish()
        }

        setupPasswordVisibility()
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

        var isConfirmPasswordVisible = false
        binding.ivShowConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                binding.etConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivShowConfirmPassword.setImageResource(R.drawable.ic_eye_hidden)
            } else {
                binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivShowConfirmPassword.setImageResource(R.drawable.ic_eye_visible)
            }
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
        }
    }

    private fun registrarUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val correo = binding.etCorreo.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        when {
            nombre.isEmpty() -> Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            correo.isEmpty() -> Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
            password.isEmpty() -> Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            confirmPassword.isEmpty() -> Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            password != confirmPassword -> Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            else -> {
                lifecycleScope.launch {
                    val usuarioExistente = database.usuarioDao().buscarPorCorreo(correo)
                    if (usuarioExistente != null) {
                        Toast.makeText(this@RegistroActivity, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                    } else {
                        val nuevoUsuario = Usuario(nombre = nombre, correo = correo, password = password)
                        database.usuarioDao().insertarUsuario(nuevoUsuario)
                        Toast.makeText(this@RegistroActivity, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
