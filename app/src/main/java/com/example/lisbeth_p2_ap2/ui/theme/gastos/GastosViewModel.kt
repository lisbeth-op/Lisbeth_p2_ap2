package com.example.lisbeth_p2_ap2.ui.theme.gastos

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GastoListState(
    val isLoading: Boolean = false,
    val gastos: List<GastoDto> = emptyList(),
    val error: String = "",
)

data class GastosState(
    val isLoading: Boolean = false,
    val gastos: GastoDto? = null,
    val error: String = ""
)

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
) : ViewModel() {
    var idGasto by mutableStateOf(1)
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf("")
    var monto by mutableStateOf(0)

    var fechaError by mutableStateOf(true)
    var ncfError by mutableStateOf(true)
    var itbisError by mutableStateOf(true)
    var monError by mutableStateOf(true)

    fun Validar(): Boolean {

        fechaError = fecha.isNotEmpty()
        ncfError = concepto.isNotEmpty()
        itbisError = ncf.isNotEmpty()
        monError = monto >0

        return !(fecha == "" || concepto == "" || ncf == "" || monto == 0)
    }

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

    val gastos: StateFlow<Resource<List<GastoDto>>> = gastoRepository.getGasto().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Resource.Loading()
    )

    var uiState = MutableStateFlow(GastoListState())
        private set
    var uiStateGasto = MutableStateFlow(GastosState())
        private set

    init {
        gastoRepository.getGasto().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    uiState.update {
                        it.copy(gastos = result.data ?: emptyList())
                    }
                }

                is Resource.Error -> {
                    uiState.update { it.copy(error = result.message ?: "Error desconocido") }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getGastoById(id: Int) {
        idGasto = id
        limpiar()
        gastoRepository.getGastoPorId(idGasto).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiStateGasto.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    uiStateGasto.update {
                        it.copy(gastos = result.data)
                    }
                    fecha = uiStateGasto.value.gastos!!.fecha
                    concepto = uiStateGasto.value.gastos!!.concepto
                    ncf = uiStateGasto.value.gastos!!.ncf
                    monto = uiStateGasto.value.gastos!!.monto!!
                }
                is Resource.Error -> {
                    uiStateGasto.update { it.copy(error = result.message ?: "Error desconocido") }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun SaveGasto() {
        viewModelScope.launch {
            val gasto = GastoDto(
                fecha = fecha,
                concepto = concepto,
                ncf = ncf,
                monto = monto
            )
            gastoRepository.postGasto(gasto)
            limpiar()
        }
    }

    fun updateGasto() {
        viewModelScope.launch {
            gastoRepository.putGasto(
                idGasto, GastoDto(
                    idGasto = idGasto,
                    fecha = fecha,
                    concepto = concepto,
                    ncf = ncf,
                    monto = monto
                )
            )
        }
    }

    fun DeleteGasto(idGasto: Int, gastos: GastoDto) {
        viewModelScope.launch {
            gastoRepository.deleteGasto(idGasto, gastos)
        }
    }

    fun limpiar() {
        fecha = ""
        concepto = ""
        ncf = ""
        monto = 0
    }
}

