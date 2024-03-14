package com.jhon.educatioapp.controllers

import android.content.Context
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
import com.jhon.educatioapp.models.LoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var apiManager: ApiManager
    // Data binding
    private lateinit var binding: ActivityLoginBinding
    private lateinit var enlace_registro: TextView
    //private var userToken: String = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

            // Obtener los datos insertados del formulario login
            val emailInicio = binding.editTextTextEmailAddress.text.toString()
            val passIncio = binding.editTextNumberPassword.text.toString()
            //val token = userToken

            // Llamamos la función para insertar datos
            insertarLogin(emailInicio, passIncio)
        }

        /*  // Realizar la transición al HomeFragment
          //val fragmentManager: FragmentManager = supportFragmentManager
          val fragmentTransaction = fragmentManager.beginTransaction()
          fragmentTransaction.replace(R.id.fragment_container, HomeFragment())
          fragmentTransaction.commit()*/
    }

    // Función para enviar datos al servidor
    private fun insertarLogin(emailInicio: String, passIncio: String) {

        // Crear una instancia de LoginData con los datos insertados en el formulario
        val data = LoginData(emailInicio, passIncio, "")

        // Llamar a la función iniciarSesion en ApiManager de forma asincrónica con lifecycleScope
        apiManager.iniciarSesion(data, object : Callback<ApiClient.LoginResponse> {
            override fun onResponse(
                call: Call<ApiClient.LoginResponse>,
                response: Response<ApiClient.LoginResponse>
            ) { // Enviar los datos al servidor

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa
                    val result = response.body()

                    // Obtener el token de la respuesta del backend
                    val token = result?.token

                    // Guardar el token en SharedPreferences
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("token", token)
                    editor.apply()

                    Toast.makeText(
                        this@LoginActivity,
                        "Datos insertados correctamente: $result",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    //finish() // Finalizar LoginActivity para que no se pueda volver atrás
                } else {
                    // Si la respuesta no es exitosa
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al iniciar sesión: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiClient.LoginResponse>, t: Throwable) {
                // Error al realizar la llamada (p.ej., sin conexión)
                Toast.makeText(
                    this@LoginActivity,
                    "Error al iniciar sesión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
