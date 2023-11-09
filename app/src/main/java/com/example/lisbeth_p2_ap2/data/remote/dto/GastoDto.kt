package com.example.lisbeth_p2_ap2.data.remote.dto

data class GastoDto (
    val idGasto: Int?=null,
    var fecha:String="",
    var concepto:String= "",
    var ncf:String="",
    var itbis: Int=0,
    var monto:Int=0

)
data class results(
    val results : List<GastoDto>
)