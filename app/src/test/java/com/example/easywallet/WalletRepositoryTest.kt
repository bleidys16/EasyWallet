package com.example.easywallet

import org.junit.Assert.*
import org.junit.Test

class WalletRepositoryTest {

    @Test
    fun addPositiveMovement_increasesBalance() {
        val initialBalance = WalletRepository.getBalance()
        val amount = 50000.0
        val movement = Movement("Test Recarga", "Hoy", "+ $ 50.000", "Completado", true, 0)
        
        WalletRepository.addMovement(movement, amount)
        
        assertEquals(initialBalance + amount, WalletRepository.getBalance(), 0.1)
    }

    @Test
    fun addNegativeMovement_decreasesBalance() {
        val initialBalance = WalletRepository.getBalance()
        val amount = 20000.0
        val movement = Movement("Test Pago", "Hoy", "- $ 20.000", "Completado", false, 0)
        
        WalletRepository.addMovement(movement, amount)
        
        assertEquals(initialBalance - amount, WalletRepository.getBalance(), 0.1)
    }

    @Test
    fun hasSufficientBalance_returnsCorrectValue() {
        val balance = WalletRepository.getBalance()
        
        assertTrue(WalletRepository.hasSufficientBalance(balance))
        assertTrue(WalletRepository.hasSufficientBalance(balance - 100))
        assertFalse(WalletRepository.hasSufficientBalance(balance + 100))
    }

    @Test
    fun getFormattedBalance_returnsExpectedFormat() {
        val value = 1234567.0
        val formatted = WalletRepository.getFormattedBalance(value)
        
        // El formato esperado es "$ 1.234.567" para es_CO
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("1.234.567"))
    }
}
