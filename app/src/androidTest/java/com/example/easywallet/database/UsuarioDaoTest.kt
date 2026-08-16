package com.example.easywallet.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UsuarioDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: UsuarioDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.usuarioDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUsuario() = runTest {
        val usuario = Usuario(nombre = "Test User", correo = "test@example.com", password = "123")
        dao.insertarUsuario(usuario)
        
        val result = dao.buscarPorCorreo("test@example.com")
        assertNotNull(result)
        assertEquals("Test User", result?.nombre)
    }

    @Test
    @Throws(Exception::class)
    fun validarLogin_withCorrectCredentials_returnsUsuario() = runTest {
        val usuario = Usuario(nombre = "Test User", correo = "login@example.com", password = "secure_password")
        dao.insertarUsuario(usuario)
        
        val result = dao.validarLogin("login@example.com", "secure_password")
        assertNotNull(result)
        assertEquals("login@example.com", result?.correo)
    }

    @Test
    @Throws(Exception::class)
    fun validarLogin_withIncorrectPassword_returnsNull() = runTest {
        val usuario = Usuario(nombre = "Test User", correo = "login@example.com", password = "secure_password")
        dao.insertarUsuario(usuario)
        
        val result = dao.validarLogin("login@example.com", "wrong_password")
        assertNull(result)
    }
}
