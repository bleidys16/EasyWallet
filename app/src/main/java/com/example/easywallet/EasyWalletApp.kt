package com.example.easywallet

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class EasyWalletApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Forzamos el modo claro globalmente desde el inicio de la aplicación
        // para evitar que las actividades se recreen al abrirse.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
