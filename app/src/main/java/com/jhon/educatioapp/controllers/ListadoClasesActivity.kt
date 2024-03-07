package com.jhon.educatioapp.controllers

import ClaseAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jhon.educatioapp.databinding.ActivityListadoClasesBinding
import com.jhon.educatioapp.models.Clase

class ListadoClasesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListadoClasesBinding
    private lateinit var claseAdapter: ClaseAdapter
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "ListadoClasesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadoClasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar conexión a Firebase
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            Log.d(TAG, "Estás conectado a Firebase.")
        } else {
            Log.d(TAG, "No estás conectado a Firebase.")
        }

        // Configurar RecyclerView
        claseAdapter = ClaseAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListadoClasesActivity)
            adapter = claseAdapter
        }

        // Configurar título
        binding.textView.text = "Clases"

        // Obtener la materia seleccionada (por ejemplo, desde un Intent)
        val materiaSeleccionada = intent.getStringExtra("materia") ?: ""

        // Escuchar cambios en la colección "clases" y actualizar el RecyclerView
        db.collection("clases")
            .orderBy("fecha", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Manejar el error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val clasesList = mutableListOf<Clase>()
                    for (doc in snapshot) {
                        val clase = doc.toObject(Clase::class.java)
                        if (clase.materia == materiaSeleccionada) {
                            clasesList.add(clase)
                        }
                    }
                    claseAdapter.updateData(clasesList)
                }
            }
    }
}
