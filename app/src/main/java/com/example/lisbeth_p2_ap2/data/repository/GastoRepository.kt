package com.example.lisbeth_p2_ap2.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.lisbeth_p2_ap2.data.GastoApi
import com.example.lisbeth_p2_ap2.data.remote.dto.GastoDto
import com.example.lisbeth_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import java.io.IOException
import android.net.http.HttpException as HttpException

class GastoRepository @Inject constructor(private val api: GastoApi) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getGastos(): Flow<Resource<List<GastoDto>>> = flow {
        try {
            emit(Resource.Loading())

            val gastos = api.getGasto()

            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar la internet"))
        }
    }



    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getGastoId(id: Int): Flow<Resource<GastoDto>> = flow {
        try {
            emit(Resource.Loading())

            val gasto =
                api.getGastoPorId(id)

            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    suspend fun postGasto(gasto: GastoDto):GastoDto= api.postGasto(gasto)
    suspend fun deleteGasto(id: Int) : GastoDto? {

        return api.delteGasto(id).body()
    }
    suspend fun putGasto(id:Int, gasto: GastoDto){
        api.putGasto(id = id, gasto = gasto)

    }
}