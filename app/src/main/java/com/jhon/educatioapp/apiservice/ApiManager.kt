package com.jhon.educatioapp.apiservice
import android.util.Log
import com.jhon.educatioapp.models.UserData
import com.jhon.educatioapp.models.DefaultResponse
import retrofit2.Response

// Paso 3: Manager de API
// Creamos una clase que actúa como un administrador para realizar solicitudes a la API.
class ApiManager(private val apiService: ApiService) {


    suspend fun insertarDatos(userDataList:UserData): ApiService.ResponseApi? {
        return try {

            val result = apiService.insertarDatos(userDataList)
            Log.e("TAG", "de: ${result} ", )
            result
        } catch (e: Exception) {
            null
        }
    }
}