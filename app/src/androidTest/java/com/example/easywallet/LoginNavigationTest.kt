package com.example.easywallet

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginNavigationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun clickRegistrarse_opensRegistroActivity() {
        // Clic en el texto de registrarse
        onView(withId(R.id.tvRegistrarse)).perform(click())

        // Verificar que estamos en la pantalla de registro
        onView(withText("REGISTRAR")).check(matches(isDisplayed()))
    }
}
