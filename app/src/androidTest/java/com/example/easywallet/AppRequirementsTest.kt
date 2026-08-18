package com.example.easywallet

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.easywallet.database.AppDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AppRequirementsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        // Opcional: Limpiar base de datos antes de las pruebas para asegurar un estado conocido
        // val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        // runBlocking {
        //    AppDatabase.getDatabase(context).clearAllTables()
        // }
    }

    @Test
    fun test01_RegistroExitoso() {
        // Navegar a Registro
        onView(withId(R.id.tvRegistrarse)).perform(click())

        // Llenar campos
        onView(withId(R.id.etNombre)).perform(typeText("Usuario Prueba"), closeSoftKeyboard())
        onView(withId(R.id.etCorreo)).perform(typeText("prueba@correo.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.etConfirmPassword)).perform(typeText("123456"), closeSoftKeyboard())

        // Clic en Registrar
        onView(withId(R.id.btnRegistrar)).perform(click())

        // Al ser exitoso, debería volver al LoginActivity
        // Verificamos que el botón de ingresar del Login esté visible
        onView(withId(R.id.btnIngresar)).check(matches(isDisplayed()))
    }

    @Test
    fun test02_RegistroDuplicado() {
        // Aseguramos que el usuario ya existe (se creó en test01)
        onView(withId(R.id.tvRegistrarse)).perform(click())

        onView(withId(R.id.etNombre)).perform(typeText("Otro Nombre"), closeSoftKeyboard())
        onView(withId(R.id.etCorreo)).perform(typeText("prueba@correo.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.etConfirmPassword)).perform(typeText("123456"), closeSoftKeyboard())

        onView(withId(R.id.btnRegistrar)).perform(click())

        // Debería quedarse en la misma pantalla (no vuelve al Login)
        onView(withId(R.id.btnRegistrar)).check(matches(isDisplayed()))
    }

    @Test
    fun test03_LoginCorrecto() {
        // Llenar campos con el usuario creado en test01
        onView(withId(R.id.etCorreo)).perform(typeText("prueba@correo.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())

        // Ingresar
        onView(withId(R.id.btnIngresar)).perform(click())

        // Verificar que estamos en Home (buscamos el saludo)
        onView(withId(R.id.tv_greeting)).check(matches(isDisplayed()))
        onView(withText("Hola, Usuario Prueba")).check(matches(isDisplayed()))
    }

    @Test
    fun test04_ContrasenaIncorrecta() {
        onView(withId(R.id.etCorreo)).perform(typeText("prueba@correo.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("654321"), closeSoftKeyboard())

        onView(withId(R.id.btnIngresar)).perform(click())

        // Debería seguir en Login
        onView(withId(R.id.btnIngresar)).check(matches(isDisplayed()))
    }

    @Test
    fun test05_CamposVaciosEnRegistro() {
        onView(withId(R.id.tvRegistrarse)).perform(click())

        // No llenamos nada y damos clic
        onView(withId(R.id.btnRegistrar)).perform(click())

        // Debería seguir en Registro
        onView(withId(R.id.btnRegistrar)).check(matches(isDisplayed()))
    }

    @Test
    fun test06_ContrasenasDiferentes() {
        onView(withId(R.id.tvRegistrarse)).perform(click())

        onView(withId(R.id.etNombre)).perform(typeText("Test Diferente"), closeSoftKeyboard())
        onView(withId(R.id.etCorreo)).perform(typeText("diferente@correo.com"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.etConfirmPassword)).perform(typeText("654321"), closeSoftKeyboard())

        onView(withId(R.id.btnRegistrar)).perform(click())

        // Debería seguir en Registro
        onView(withId(R.id.btnRegistrar)).check(matches(isDisplayed()))
    }
}
