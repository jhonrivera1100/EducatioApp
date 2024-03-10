package com.jhon.educatioapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jhon.educatioapp.R
import com.jhon.educatioapp.databinding.FragmentMisClasesDocenteBinding
import com.jhon.educatioapp.models.Clase
import java.text.SimpleDateFormat
import java.util.Locale

class MisClasesDocenteFragment : Fragment() {

    private lateinit var binding: FragmentMisClasesDocenteBinding
    private val db = FirebaseFirestore.getInstance()
    private val clasesCollection = db.collection("clases")
    private val clasesList = mutableListOf<Clase>()
    private val adapter = ClaseAdapter(clasesList)
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisClasesDocenteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        signInWithTestUser()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewClases.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClases.adapter = adapter
    }

    private fun signInWithTestUser() {
        auth.signInWithEmailAndPassword("Prueba123@prueba.com", "123456789")
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Usuario autenticado correctamente, ahora podemos obtener las clases desde Firestore
                    Log.d(TAG, "signInWithEmail:success")
                    obtenerClasesDesdeFirestore()
                } else {
                    // Error al iniciar sesión
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun obtenerClasesDesdeFirestore() {
        // Eliminar el filtro whereEqualTo para obtener todos los registros del usuario
        clasesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                // Limpiar la lista antes de agregar nuevas clases
                clasesList.clear()
                for (document in querySnapshot.documents) {
                    val data = document.data
                    val clase = Clase(
                        data?.get("materia") as String,
                        data["tema"] as String,
                        (data["fecha"] as com.google.firebase.Timestamp).toDate(), // Convertir Timestamp a Date
                        data["hora"] as String,
                        data["modalidad"] as String,
                        data["valorClase"] as String
                    )
                    clasesList.add(clase)
                }
                adapter.notifyDataSetChanged()

                // Mostrar las clases obtenidas en el log
                for (clase in clasesList) {
                    Log.d(TAG, "Clase obtenida: $clase")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al obtener las clases", exception)
                Toast.makeText(requireContext(), "Error al obtener las clases", Toast.LENGTH_SHORT).show()
            }
    }
    private inner class ClaseAdapter(private val clases: List<Clase>) :
        RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_clase_docente, parent, false)
            return ClaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
            val clase = clases[position]
            holder.bind(clase)
        }

        override fun getItemCount() = clases.size

        inner class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Inicializar vistas
            private val textViewMateria: TextView = itemView.findViewById(R.id.textViewMateria)
            private val textViewTema: TextView = itemView.findViewById(R.id.textViewTema)
            private val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
            private val textViewHora: TextView = itemView.findViewById(R.id.textViewHora)
            private val textViewModalidad: TextView = itemView.findViewById(R.id.textViewModalidad)
            private val textViewValorClase: TextView = itemView.findViewById(R.id.textViewValorClase)
            private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)

            init {
                // Agregar OnClickListener al botón de aceptar en la inicialización del ViewHolder
                btnAccept.setOnClickListener {
                    // Mostrar mensaje de clase aceptada con éxito
                    Toast.makeText(itemView.context, "Clase aceptada con éxito", Toast.LENGTH_SHORT).show()
                    // Aquí puedes agregar cualquier otra tarea que desees ejecutar al hacer clic en el botón
                }
            }
            fun bind(clase: Clase) {
                // Asignar valores a las vistas
                textViewMateria.text = clase.materia
                textViewTema.text = clase.tema
                val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                val fechaFormateada = dateFormat.format(clase.fecha)
                textViewFecha.text = fechaFormateada
                textViewHora.text = clase.hora
                textViewModalidad.text = clase.modalidad
                textViewValorClase.text = clase.valorClase
            }
        }
    }

    companion object {
        private const val TAG = "MisClasesDocente"
    }
}
