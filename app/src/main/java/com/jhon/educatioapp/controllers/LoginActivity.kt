package com.jhon.educatioapp.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jhon.educatioapp.R
import android.widget.TextView
import com.jhon.educatioapp.apiservice.ApiClient
import com.jhon.educatioapp.apiservice.ApiManager
import com.jhon.educatioapp.databinding.ActivityLoginBinding
import com.jhon.educatioapp.databinding.ActivityRegistroBinding
import com.jhon.educatioapp.models.LoginData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            // Llamamos la función para insertar datos
            insertarLogin()
        }
    }

    // Función para enviar datos al servidor
    private fun insertarLogin() {
        // Obtener los datos insertados del formulario login
        val emailInicio = binding.editTextTextEmailAddress.text.toString()
        val passIncio = binding.editTextNumberPassword.text.toString()

        // Crear una instancia de LoginData con los datos insertados en el formulario
        val data = LoginData(email = emailInicio, password = passIncio)

        // Llamar a la función iniciarSesion en ApiManager de forma asincrónica con lifecycleScope
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Enviar los datos al servidor
                val result = apiManager.iniciarSesion(data)
                Log.e(TAG, "$result")
                Log.i(TAG, "Solicitud POST exitosa: Datos insertados correctamente")

                // Mostrar un mensaje de éxito en la aplicación
                Toast.makeText(
                    this@LoginActivity, "Datos insertados correctamente", Toast.LENGTH_SHORT
                ).show()

                // Manejar el inicio de sesión exitoso y navegar al fragmento deseado
                handleSuccessfulLogin()

            } catch (e: Exception) {
                // Manejar errores y mostrar un mensaje de error en la aplicación
                Log.e(TAG, "Error al procesar la solicitud POST: ${e.message}")
                Toast.makeText(
                    this@LoginActivity, "Error al insertar datos: ${e.message}", Toast.LENGTH_SHORT
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
