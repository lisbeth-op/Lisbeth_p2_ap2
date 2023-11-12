package com.example.lisbeth_p2_ap2.data


import com.example.lisbeth_p2_ap2.data.remote.dto.GastoDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastoApi {
    @GET("api/Gastos")
    suspend fun getGasto(): List<GastoDto>
    @GET("api/Gastos/{id}")
    suspend fun getGastoPorId(@Path("id") id: Int): GastoDto

    @POST("api/Gastos")
    suspend fun postGasto(@Body gasto: GastoDto) :GastoDto

    @PUT("api/Gastos/{id}")
    suspend fun putGasto(@Path("id") id:Int, @Body gasto: GastoDto): Response<Unit>

    @DELETE("api/Gastos/{id}")
    suspend fun delteGasto(@Path("id") id:Int,): Response<GastoDto>
}