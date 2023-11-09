package com.example.lisbeth_p2_ap2.data.repository

import android.net.http.HttpException
import com.example.lisbeth_p2_ap2.data.GastoApi
import com.example.lisbeth_p2_ap2.data.remote.dto.GastoDto
import com.example.lisbeth_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import java.io.IOException

class GastoRepository @Inject constructor(private val api: GastoApi) {

    fun getGasto(): Flow<Resource<List<GastoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val gasto = api.getGasto()
            emit(Resource.Success(gasto.results))
        } catch (e: retrofit2.HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    fun getGastoPorId(id: Int): Flow<Resource<GastoDto>> = flow {
        try {
            emit(Resource.Loading())
            val gastos = api.getGastoPorId(id)
            emit(Resource.Success(gastos))
        } catch (e:retrofit2.HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    suspend fun postGasto(gastoDto: GastoDto) {
        try {
            api.postGasto(gastoDto)
        } catch (e: retrofit2.HttpException) {
            throw Exception(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            throw Exception(e.message ?: "verificar tu conexion a internet")
        }
    }

    suspend fun deleteGasto(id: Int, gastoDto: GastoDto) {
        try {
            api.delteGasto(id, gastoDto)
        } catch (e: retrofit2.HttpException) {
            throw Exception(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            throw Exception(e.message ?: "verificar tu conexion a internet")
        }
    }

    suspend fun putGasto(id:Int, gastoDto: GastoDto) {
        try {
            api.putGasto(id, gastoDto)
        } catch (e: retrofit2.HttpException) {
            throw Exception(e.message ?: "Error HTTP GENERAL")
        } catch (e: IOException) {
            throw Exception(e.message ?: "verificar tu conexion a internet")
        }
    }
}
