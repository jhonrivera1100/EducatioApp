
package com.jhon.educatioapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jhon.educatioapp.R
import com.jhon.educatioapp.models.NotificationItem
import java.text.SimpleDateFormat
import java.util.Locale
import android.content.Context
import com.jhon.educatioapp.controllers.RegistroActivity.Companion.TAG

class MisNotificacionesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private val notificationList = mutableListOf<NotificationItem>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mis_notificaciones, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewNotificaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter(notificationList)
        recyclerView.adapter = adapter
        obtenerPostulacionesUsuarioLogueado()
        return view
    }

    private fun obtenerPostulacionesUsuarioLogueado() {
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            db.collection("postulaciones")
                .whereEqualTo("email", currentUserEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    notificationList.clear()
                    for (document in querySnapshot.documents) {
                        // Obtener el objeto aceptaciones de Firestore
                        val aceptacionesObject = document.get("aceptaciones")

                        // Verificar si aceptacionesObject es una lista
                        if (aceptacionesObject is List<*>) {
                            val aceptaciones = aceptacionesObject as? List<*>

                            if (!aceptaciones.isNullOrEmpty()) {
                                for (aceptacion in aceptaciones) {
                                    when (aceptacion) {
                                        is HashMap<*, *> -> {
                                            val correoPostulante = aceptacion["correo"] as? String
                                            val nombre = aceptacion["nombre"] as? String
                                            val telefono = aceptacion["telefono"] as? String
                                            val fechaPostulacionTimestamp = aceptacion["fechaPostulacion"] as? com.google.firebase.Timestamp

                                            if (correoPostulante != null && nombre != null && telefono != null && fechaPostulacionTimestamp != null) {
                                                val fechaPostulacion = fechaPostulacionTimestamp.toDate()
                                                val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                                                val fechaFormateada = dateFormat.format(fechaPostulacion)

                                                val notificationItem = NotificationItem(fechaFormateada, correoPostulante, nombre, telefono)
                                                notificationList.add(notificationItem)
                                            } else {
                                                Log.e(TAG, "Algunos campos son nulos en la aceptación: $aceptacion")
                                            }
                                        }
                                        is String -> {
                                            Log.e(TAG, "Elemento de la lista 'aceptaciones' es una cadena de texto: $aceptacion")
                                        }
                                        else -> {
                                            Log.e(TAG, "Elemento de la lista 'aceptaciones' no es un HashMap ni una cadena de texto: $aceptacion")
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "La lista 'aceptaciones' está vacía")
                            }
                        } else {
                            Log.e(TAG, "El objeto 'aceptaciones' no es una lista: $aceptacionesObject")
                        }
                    }
                    if (notificationList.isEmpty()) {
                        Toast.makeText(requireContext(), "No hay postulaciones nuevas", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error al obtener las postulaciones", exception)
                    Toast.makeText(requireContext(), "Error al obtener las postulaciones", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Usuario no logueado
        }
    }

    private inner class NotificationAdapter(private val notificationList: List<NotificationItem>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notificaciones, parent, false)
            return NotificationViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
            val notification = notificationList[position]
            holder.bind(notification)
        }

        override fun getItemCount(): Int {
            return notificationList.size
        }

        inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val fechaTextView: TextView = itemView.findViewById(R.id.textViewFecha)
            private val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
            private val nombreTextView: TextView = itemView.findViewById(R.id.textViewName)
            private val telefonoTextView: TextView = itemView.findViewById(R.id.textViewTelephone)

            fun bind(notification: NotificationItem) {
                fechaTextView.text = notification.fecha
                emailTextView.text = "Correo: ${notification.email}"
                nombreTextView.text = "Nombre: ${notification.nombre}"
                telefonoTextView.text = "Telefono: ${notification.telefono}"
            }
        }
    }
}