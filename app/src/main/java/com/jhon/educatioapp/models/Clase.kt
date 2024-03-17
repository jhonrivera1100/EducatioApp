package com.jhon.educatioapp.models

import java.util.Date

data class Clase(
    val email: String,
    val materia: String,
    val tema: String,
    val fecha: Date,
    val horaInicio: String,
    val horaFin: String,
    val modalidad: String,
    val valorClase: String
)
