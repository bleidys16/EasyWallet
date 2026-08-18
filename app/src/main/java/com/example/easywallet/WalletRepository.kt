package com.example.easywallet

import java.text.NumberFormat
import java.util.Locale

object WalletRepository {

    private var balance: Double = 4775000.0 // Saldo inicial predeterminado

    fun getBalance(): Double = balance

    fun setBalance(newBalance: Double) {
        balance = newBalance
    }

    fun addMovement(movement: Movement, amount: Double) {
        if (movement.isPositive) {
            balance += amount
        } else {
            balance -= amount
        }
    }

    fun hasSufficientBalance(amount: Double): Boolean {
        return amount <= balance
    }

    fun getFormattedBalance(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("$", "$ ").replace(",", ".")
    }
}
