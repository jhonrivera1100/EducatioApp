package com.jhon.educatioapp.controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jhon.educatioapp.R
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    private lateinit var enlace_registro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa tu TextView
        enlace_registro = findViewById(R.id.enlace_registro)

        // Establece un listener de clics en tu TextView
        enlace_registro.setOnClickListener {
            // Inicia la actividad de registro de usuario
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
