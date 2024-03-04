package com.jhon.educatioapp.controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jhon.educatioapp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Aquí implementa tu lógica de inicio de sesión
        // Por ejemplo, si el inicio de sesión es exitoso, navega a MainActivity
        val loggedIn = checkIfLoggedIn() // Función ficticia para verificar si el usuario ya está conectado

        if (loggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Termina LoginActivity para que el usuario no pueda volver atrás
        }
    }

    private fun checkIfLoggedIn(): Boolean {
        // Implementa tu lógica de inicio de sesión aquí
        // Por ejemplo, verifica si el usuario ya está autenticado
        // Si el usuario ya está autenticado, devuelve true, de lo contrario, devuelve false
        return false
    }
}