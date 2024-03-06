package com.jhon.educatioapp.apiservice
import android.util.Log
import com.jhon.educatioapp.models.UserData
import com.jhon.educatioapp.models.DefaultResponse
import com.jhon.educatioapp.models.LoginData
import retrofit2.Response

// Paso 3: Manager de API
// Creamos una clase que actúa como un administrador para realizar solicitudes a la API.

//creamos la instancia como parametro al constructor de ApiManager
class ApiManager(private val apiService: ApiService) {

    // Función para realizar la solicitud de inicio de sesión
    suspend fun iniciarSesion(loginData: LoginData): ApiService.ResponseApi? {
        return try {
            // Realizar la solicitud de inicio de sesión utilizando el servicio API
            val response = apiService.login(loginData)
            Log.e("TAG", "de: ${response} ", )
            // Retornar la respuesta obtenida
            response
        } catch (e: Exception) {
            null
        }
    }

    // Función para realizar la solicitud de registro de usuario
    suspend fun insertarDatos(userDataList:UserData): ApiService.ResponseApi? {
        return try {

            val result = apiService.insertarDatos(userDataList)
            //respuesta
            Log.e("TAG", "de: ${result} ", )
            result
        } catch (e: Exception) {
            null
        }
    }
}