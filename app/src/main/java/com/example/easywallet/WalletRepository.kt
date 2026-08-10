package com.example.easywallet

import java.text.NumberFormat
import java.util.*

/**
 * Singleton repository to manage wallet data across activities.
 */
object WalletRepository {
    private var balance: Double = 4775000.0
    private val _movements = mutableListOf<Movement>().apply {
        add(Movement("Pago Netflix", "10 Ago 2026 • 09:42 a. m.", "- $ 25.900", "Completado", false, R.drawable.ic_netflix))
        add(Movement("Recarga Nequi", "09 Ago 2026 • 07:30 p. m.", "- $ 30.000", "Completado", false, R.drawable.ic_transfer_modern))
        add(Movement("Transferencia recibida", "08 Ago 2026 • 02:15 p. m.", "+ $ 250.000", "Completado", true, R.drawable.ic_arrow_incoming))
        add(Movement("Nómina", "05 Ago 2026 • 08:30 a. m.", "+ $ 3.200.000", "Completado", true, R.drawable.ic_arrow_incoming))
        add(Movement("Pago Spotify", "03 Ago 2026 • 09:12 a. m.", "- $ 25.000", "Completado", false, R.drawable.ic_spotify))
    }
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