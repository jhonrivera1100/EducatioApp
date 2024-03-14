package com.jhon.educatioapp.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jhon.educatioapp.R
import com.jhon.educatioapp.apiservice.ApiClient
import com.jhon.educatioapp.apiservice.ApiManager
import com.jhon.educatioapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var apiManager: ApiManager

    // Data binding
    private lateinit var binding: ActivityLoginBinding
    private lateinit var enlace_registro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Data binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Abrir RegistroActivity
        enlace_registro = binding.enlaceRegistro
        enlace_registro.setOnClickListener {
            // Inicia la actividad de registro de usuario
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Creación del ApiService
        apiManager = ApiManager(ApiClient.createApiService())

        // Configurar el botón de enviar datos del login
        binding.bottonInicioDeSesion.setOnClickListener {
            // Llamamos la función para iniciar sesión
            iniciarSesion()
        }
    }

    // Función para iniciar sesión
    private fun iniciarSesion() {
        // Obtener los datos insertados del formulario de inicio de sesión
        val emailInicio = binding.editTextTextEmailAddress.text.toString()
        val passIncio = binding.editTextNumberPassword.text.toString()

        // Iniciar sesión en Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInicio, passIncio)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    Log.d(TAG, "Inicio de sesión en Firebase exitoso para el correo: $emailInicio")

                    // Mostrar mensaje de éxito en la aplicación
                    Toast.makeText(
                        this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT
                    ).show()

                    // Manejar el inicio de sesión exitoso y navegar al fragmento deseado
                    handleSuccessfulLogin()
                } else {
                    // Error en el inicio de sesión
                    Log.e(TAG, "Error en el inicio de sesión en Firebase: ${task.exception?.message}")

                    // Mostrar mensaje de error en la aplicación
                    Toast.makeText(
                        this@LoginActivity, "Error en el inicio de sesión: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Función para manejar el inicio de sesión exitoso y navegar a MainActivity
    private fun handleSuccessfulLogin() {
        // Crear un Intent para iniciar MainActivity
        val intent = Intent(this, MainActivity::class.java)
        // Iniciar MainActivity
        startActivity(intent)
        // Finalizar LoginActivity para que no se pueda volver atrás
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}
