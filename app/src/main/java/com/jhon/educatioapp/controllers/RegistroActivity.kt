package com.jhon.educatioapp.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.jhon.educatioapp.apiservice.ApiClient
import com.jhon.educatioapp.apiservice.ApiManager
import com.jhon.educatioapp.databinding.ActivityRegistroBinding
import com.jhon.educatioapp.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    private lateinit var apiManager: ApiManager

    // Data binding
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Data binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creación del ApiService
        apiManager = ApiManager(ApiClient.createApiService())

        // Configurar el botón de registro
        binding.botonRegistro.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val email = binding.email.text.toString()
        val contrasena = binding.contrasena.text.toString()
        val confirmarContrasena = binding.confirmarContrasena.text.toString()

        // Validación de confirmación de contraseña
        if (contrasena != confirmarContrasena) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(
                this@RegistroActivity,
                "Las contraseñas no coinciden",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso en Firebase Authentication
                    Log.d(TAG, "Registro exitoso en Firebase Authentication")

                    // Llamamos a la función para insertar datos en el servidor
                    insertarDatos(email, contrasena)
                } else {
                    // Error en el registro en Firebase Authentication
                    Log.e(TAG, "Error en el registro en Firebase Authentication: ${task.exception?.message}")
                    Toast.makeText(
                        this@RegistroActivity, "Error en el registro: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun insertarDatos(email: String, contrasena: String) {
        // Obtenemos los datos del formulario
        val identificacion = binding.identificacion.text.toString()
        val name = binding.name.text.toString()
        val ciudad = binding.ciudad.text.toString()
        val telefono = binding.telefono.text.toString()

        // Creamos una instancia de UserData con los datos del formulario
        val data = UserData(
            N_Identificacion = identificacion,
            NomCompleto = name,
            Telefono = telefono,
            Ciudad = ciudad,
            email = email,
            password = contrasena
        )

        // Llamamos a la función insertarDatos en ApiManager de forma asincrónica con lifecycleScope
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Enviamos los datos al servidor
                val result = apiManager.insertarDatos(data)
                Log.i(TAG, "Solicitud POST exitosa: Datos insertados correctamente en el servidor")

                // Manejar respuesta exitosa
                Toast.makeText(
                    this@RegistroActivity,
                    "Registro exitoso",
                    Toast.LENGTH_SHORT
                ).show()

                // Redirigir al usuario a la actividad de inicio de sesión
                val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                // Manejar error en el servidor
                Log.e(TAG, "Error al procesar la solicitud POST: ${e.message}")

                Toast.makeText(
                    this@RegistroActivity,
                    "Error al insertar datos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val TAG = "RegistroActivity"
    }
}
