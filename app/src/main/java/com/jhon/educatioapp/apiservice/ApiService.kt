package com.jhon.educatioapp.apiservice
import com.jhon.educatioapp.models.UserData
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Paso 2: Interfaz de Servicio
interface ApiService {

    data class ResponseApi(
        val message:ArrayList<String>
    )


    //@Body se usa para indicar que los datos que se enviarán en el cuerpo de la solicitud serán proporcionados por el parámetro
    @POST("register")
    suspend fun insertarDatos(@Body userList: UserData): ResponseApi//void es para no esperar ninguna respuesta

    /* @GET("https://bdeducatio.vercel.app/api/register")
     suspend fun register(
         // Aquí puedes agregar cualquier parámetro necesario para la solicitud de registro
     ): RegisterRespuesta
 */

    /* @GET("https://bdeducatio.vercel.app/api/login")
     suspend fun login(
         // Aquí puedes agregar cualquier parámetro necesario para la solicitud de inicio de sesión
     ): LoginRespuesta
 */
}
