package com.jhon.educatioapp.apiservice

import com.example.listado_solicitud.misclasesdocente.ListadoClases
import com.example.listado_solicitud.misclasesdocente.ListadoClasesRespuesta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ListadoClasesApiService{
    //@GET("listado_clases/{listado_clases}")

    fun getListadoClasesByID(@Path("pokemon") id:String): Call<ListadoClases>

    @GET("pokemon")
    fun getListadoClases() : Call<ListadoClasesRespuesta>

}
