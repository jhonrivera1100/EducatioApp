package com.jhon.educatioapp.controllers

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jhon.educatioapp.R
import com.jhon.educatioapp.apiservice.ApiClient
import com.jhon.educatioapp.apiservice.ApiManager
import com.jhon.educatioapp.databinding.ActivityRegistroBinding
import com.jhon.educatioapp.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

        // Configurar el botón de enviar formulario
        binding.botonRegistro.setOnClickListener {
            // Llamamos la función para insertar datos
            insertarDatos()
        }
    }

    // Función para enviar datos al servidor
    private fun insertarDatos() {
        // Obtenemos los datos del formulario a través del binding
        val identificacion = binding.identificacion.text.toString()
        val name = binding.name.text.toString()
        //val nacimiento = binding.fechaN.text.toString()
        val ciudad = binding.ciudad.text.toString()
        val telefono = binding.telefono.text.toString()
        val email = binding.email.text.toString()
        val contrasena = binding.contrasena.text.toString()

        // Creamos una lista de UserData con los datos del formulario

        val data = UserData(
            N_Identificacion = identificacion,
            NomCompleto = name,
            Telefono = telefono,
            Ciudad = ciudad,
            email = email,
            password = contrasena
        )

        // Llamamos a la función insertarDatos en ApiManager de forma asincrónica con GlobalScope
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Enviamos los datos al servidor
                val result = apiManager.insertarDatos(data)
                Log.e(TAG, "${result}", )

                Log.i(TAG, "Solicitud POST exitosa: Datos insertados correctamente")

                // Manejar respuesta exitosa, por ejemplo, mostrar un mensaje al usuario
                Toast.makeText(
                    this@RegistroActivity,
                    "Datos insertados correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error al procesar la solicitud POST: ${e.message}")

                // Manejar errores, por ejemplo, mostrar un mensaje de error al usuario
                Toast.makeText(
                    this@RegistroActivity,
                    "Error al insertar datos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}