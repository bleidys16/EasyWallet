package com.example.easywallet

import java.text.NumberFormat
import java.util.*

/**
 * Singleton repository to manage wallet data across activities.
 */
object WalletRepository {
    private var balance: Double = 4580000.0
    private val _movements = mutableListOf<Movement>()
    private var notificationCount = 0
    private var lastNotificationMessage = ""

    fun getBalance(): Double = balance

    fun getFormattedBalance(value: Double = balance): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        return format.format(value).replace("$", "$ ").replace(",", ".")
    }

    fun getMovements(): List<Movement> = _movements.reversed()

    fun addMovement(movement: Movement, amount: Double) {
        if (movement.isPositive) {
            balance += amount
            notificationCount++
            lastNotificationMessage = "Has recibido una recarga de ${getFormattedBalance(amount)}"
        } else {
            balance -= amount
        }
        _movements.add(movement)
    }

    fun hasSufficientBalance(amount: Double): Boolean {
        return balance >= amount
    }

    fun getNotificationCount() = notificationCount
    fun clearNotifications() {
        notificationCount = 0
    }
}