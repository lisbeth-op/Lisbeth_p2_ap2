package com.example.lisbeth_p2_ap2.ui.theme.gastos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lisbeth_p2_ap2.data.remote.dto.GastoDto
import com.example.lisbeth_p2_ap2.data.repository.GastoRepository
import com.example.lisbeth_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class gastosListState(
    val isLoading: Boolean = false,
    val Gastos: List<GastoDto> = emptyList(),
    val error: String = ""
)


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class GastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
) : ViewModel() {
    var fecha by mutableStateOf("")
    var idSuplidor by mutableStateOf("")
    var suplidor by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf("")
    var monto by mutableStateOf("")
    var id by mutableStateOf(0)

    var fechaError by mutableStateOf(false)
    var idSuplidorError by mutableStateOf(false)
    var conceptoError by mutableStateOf(false)
    var ncfError by mutableStateOf(false)
    var itbisError by mutableStateOf(false)
    var montoError by mutableStateOf(false)

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()
    var mensaje by mutableStateOf("")

    private var _state = MutableStateFlow(gastosListState())
    val state: StateFlow<gastosListState> = _state.asStateFlow()


    fun onFechaChange(valor: String) {
        fecha = valor
        fechaError = valor.isNullOrBlank()
    }

    fun onIdSuplidorChange(valor: String) {
        idSuplidor = valor
        idSuplidorError = valor.isNullOrBlank()
    }

    fun onConceptoChange(valor: String) {
        concepto = valor
        conceptoError = valor.isNullOrBlank()
    }

    fun onNcfChange(valor: String) {
        ncf = valor
        ncfError = valor.isNullOrBlank()
    }

    fun onItbisChange(valor: String) {
        itbis = valor
        itbisError = valor.isNullOrBlank()
    }

    fun onMontoChange(valor: String) {
        monto = valor
        montoError = valor.isNullOrBlank()
    }

    fun Load() {
        gastoRepository.getGastos().onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> {
                    gastosListState(isLoading = true)
                }

                is Resource.Success -> {
                    gastosListState(Gastos = result.data ?: emptyList(), isLoading = false)
                }

                is Resource.Error -> {
                    gastosListState(
                        error = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    init {
        Load()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun GuardarGasto() {
        viewModelScope.launch {

            if (!validar()) {
                var gastos = GastoDto(
                    idGasto = id,
                    fecha = fecha,
                    idSuplidor = idSuplidor.toIntOrNull() ?: 0,
                    suplidor = "",
                    concepto = concepto,
                    ncf = ncf,
                    itbis = itbis.toIntOrNull() ?: 0,
                    monto = monto.toIntOrNull() ?: 0
                )

                if (id != 0) {
                    gastoRepository.putGasto(gastos.idGasto!!, gastos)

                } else {
                    gastoRepository.postGasto(gastos)

                }
                mensaje = "Guardado"
                limpiar()
                Load()



            } else {
                mensaje = "Error"
            }
        }

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun DeleteGasto(id: Int) {
        viewModelScope.launch {
            gastoRepository.deleteGasto(id)
            Load()
        }

    }

    fun validar(): Boolean {
        onFechaChange(fecha) //+"T00:00:00.00.120"
        onIdSuplidorChange(idSuplidor)
        onConceptoChange(concepto)
        onNcfChange(ncf)
        onItbisChange(itbis)
        onMontoChange(monto)
        return fechaError || idSuplidorError || conceptoError || ncfError || itbisError || montoError
    }

    fun limpiar() {
        fecha = ""
        idSuplidor = ""
        suplidor = ""
        concepto = ""
        ncf = ""
        itbis = ""
        monto = ""
        id = 0
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun update(gastos: GastoDto) {
        id = gastos.idGasto!!
        fecha = gastos.fecha.toString()
        idSuplidor = gastos.idSuplidor.toString()
        suplidor = gastos.suplidor.toString()
        concepto = gastos.concepto
        ncf = gastos.ncf.toString()
        itbis = gastos.itbis.toString()
        monto = gastos.monto.toString()
        Load()


    }

}