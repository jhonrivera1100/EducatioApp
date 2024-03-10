package com.jhon.educatioapp.models

import java.util.Date

data class Clase(
    val materia: String,
    val tema: String,
    val fecha: Date,
    val hora: String,
    val modalidad: String,
    val valorClase: String
)
