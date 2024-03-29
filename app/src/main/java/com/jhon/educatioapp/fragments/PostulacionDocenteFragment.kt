package com.jhon.educatioapp.fragments

import android.app.AlertDialog
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
import com.jhon.educatioapp.R
import com.jhon.educatioapp.databinding.FragmentPostulacionDocenteBinding
import com.jhon.educatioapp.models.Clase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context


class PostulacionDocenteFragment : Fragment() {

    private lateinit var binding: FragmentPostulacionDocenteBinding
    private val db = FirebaseFirestore.getInstance()
    private val clasesCollection = db.collection("clases")
    private val clasesList = mutableListOf<Clase>()
    private val adapter = ClaseAdapter(clasesList)
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostulacionDocenteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        obtenerClasesDesdeFirestore() // Llamada para obtener las clases desde Firestore

        val datosUsuario = obtenerDatosUsuarioSharedPreferences(requireContext())
        if (datosUsuario == null) {
            Toast.makeText(requireContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewClases.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClases.adapter = adapter
    }

    private fun obtenerClasesDesdeFirestore() {
        clasesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                // Limpiar la lista antes de agregar nuevas clases
                clasesList.clear()
                for (document in querySnapshot.documents) {
                    val data = document.data
                    val email = data?.get("email") as? String ?: ""
                    val materia = data?.get("materia") as? String ?: ""
                    val tema = data?.get("tema") as? String ?: ""
                    val fecha = (data?.get("fecha") as? com.google.firebase.Timestamp)?.toDate() ?: Date()
                    val horaInicio = data?.get("horainicio") as? String ?: ""
                    val horaFin = data?.get("horafin") as? String ?: ""
                    val modalidad = data?.get("modalidad") as? String ?: ""
                    val valorClase = data?.get("valorClase") as? String ?: ""
                    val clase = Clase(
                        email,
                        materia,
                        tema,
                        fecha,
                        horaInicio,
                        horaFin,
                        modalidad,
                        valorClase
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
                .inflate(R.layout.item_postulacion_docente, parent, false)
            return ClaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
            val clase = clases[position]
            holder.bind(clase)
        }

        override fun getItemCount() = clases.size

        inner class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Inicializar vistas
            private val tvListadoClasesName: TextView = itemView.findViewById(R.id.tvListadoClasesName)
            private val textViewMateria: TextView = itemView.findViewById(R.id.textViewMateria)
            private val textViewTema: TextView = itemView.findViewById(R.id.textViewTema)
            private val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
            private val textViewHoraInicio: TextView = itemView.findViewById(R.id.textViewHoraInicio)
            private val textViewHoraFin: TextView = itemView.findViewById(R.id.textViewHoraFin)
            private val textViewModalidad: TextView = itemView.findViewById(R.id.textViewModalidad)
            private val textViewValorClase: TextView = itemView.findViewById(R.id.textViewValorClase)
            private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)

            init {
                btnAccept.setOnClickListener {
                    val clase = clases[adapterPosition]
                    val currentUserEmail = auth.currentUser?.email
                    if (currentUserEmail != null) {
                        // Agregar la aceptación a Firestore
                        agregarAceptacion(clase, currentUserEmail)
                    } else {
                        Toast.makeText(itemView.context, "Usuario no logueado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            fun bind(clase: Clase) {
                // Asignar valores a las vistas
                tvListadoClasesName.text = clase.email
                textViewMateria.text = clase.materia
                textViewTema.text = clase.tema
                val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                val fechaFormateada = dateFormat.format(clase.fecha)
                textViewFecha.text = fechaFormateada
                textViewHoraInicio.text = clase.horaInicio
                textViewHoraFin.text = clase.horaFin
                textViewModalidad.text = clase.modalidad
                textViewValorClase.text = clase.valorClase
            }
        }
    }

    private fun obtenerDatosUsuarioSharedPreferences(context: Context): Pair<String, String>? {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", null)
        val telefono = sharedPreferences.getString("telefono", null)

        return if (nombre != null && telefono != null) {
            Pair(nombre, telefono)
        } else {
            null
        }
    }


    private fun agregarAceptacion(clase: Clase, currentUserEmail: String) {
        val datosUsuario = obtenerDatosUsuarioSharedPreferences(requireContext())
        if (datosUsuario == null) {
            Toast.makeText(requireContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
            return
        }
        val (nombre, telefono) = datosUsuario

        val fechaTimestamp = clase.fecha
        val fechaFirestore = com.google.firebase.Timestamp(fechaTimestamp)
        val fechaActual = Date() // Obtener la fecha actual

        val postulacionData = hashMapOf(
            "email" to clase.email,
            "materia" to clase.materia,
            "tema" to clase.tema,
            "fecha" to fechaFirestore,
            "horainicio" to clase.horaInicio,
            "horafin" to clase.horaFin,
            "modalidad" to clase.modalidad,
            "valorClase" to clase.valorClase,
            "aceptaciones" to listOf(
                hashMapOf(
                    "correo" to currentUserEmail,
                    "nombre" to nombre,
                    "telefono" to telefono,
                    "fechaPostulacion" to fechaActual // Agregar la fecha actual como "fechaPostulacion"
                )
            )
        )

        db.collection("postulaciones")
            .add(postulacionData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Documento agregado con ID: ${documentReference.id}")
                // Crear un AlertDialog personalizado
                val builder = AlertDialog.Builder(requireContext())
                val inflater = requireActivity().layoutInflater
                val dialogView = inflater.inflate(R.layout.popup_postular, null)

// Configurar el AlertDialog con el diseño personalizado
                builder.setView(dialogView)
                val dialog = builder.create()

// Manejar el clic del botón OK
                dialogView.findViewById<Button>(R.id.buttonOK).setOnClickListener {
                    // Cerrar el cuadro de diálogo
                    dialog.dismiss()
                }

// Mostrar el AlertDialog
                dialog.show()

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al agregar la aceptación", e)
                Toast.makeText(requireContext(), "Error al guardar la aceptación", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "PostulacionDocente"
    }
}