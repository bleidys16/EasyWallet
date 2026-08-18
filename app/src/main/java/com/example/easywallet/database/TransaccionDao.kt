package com.example.easywallet.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransaccionDao {
    @Insert
    suspend fun insertarTransaccion(transaccion: Transaccion)

    @Query("SELECT * FROM transacciones WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun obtenerTransaccionesPorUsuario(usuarioId: Int): List<Transaccion>

    @Query("SELECT SUM(CASE WHEN tipo = 'INGRESO' THEN monto ELSE -monto END) FROM transacciones WHERE usuarioId = :usuarioId")
    suspend fun obtenerSaldoPorUsuario(usuarioId: Int): Double?
}
