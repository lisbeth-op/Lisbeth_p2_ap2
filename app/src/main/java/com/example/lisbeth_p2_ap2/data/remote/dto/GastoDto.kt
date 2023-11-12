package com.example.lisbeth_p2_ap2.data.remote.dto

data class GastoDto (
    val idGasto: Int?=null,
    var fecha: String = "",
    var ncf: String = "",
    var idSuplidor: Int?= null,
    var suplidor:String = "",
    var concepto: String = "",
    var itbis: Int?= 0,
    var monto: Int?= 0,
)
