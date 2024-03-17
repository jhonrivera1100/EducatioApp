package com.jhon.educatioapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jhon.educatioapp.R
import com.jhon.educatioapp.models.NotificationItem
import java.text.SimpleDateFormat
import java.util.Locale

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
                        val fechaTimestamp = document.getTimestamp("fecha")
                        val fecha = fechaTimestamp?.toDate()

                        // Formatear la fecha
                        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                        val fechaFormateada = dateFormat.format(fecha)


                        // Obtener la matriz de aceptaciones
                        val aceptaciones = document.get("aceptaciones") as? List<String>
                        // Verificar si la matriz de aceptaciones no es nula y no está vacía
                        if (!aceptaciones.isNullOrEmpty()) {
                            // Tomar el primer correo de la lista como el correo del postulante
                            val correoPostulante = aceptaciones[0]
                            // Crear el objeto NotificationItem utilizando el correo del postulante
                            val notificationItem = NotificationItem(fecha.toString(), correoPostulante)
                            notificationList.add(notificationItem)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Manejar la falla al obtener las postulaciones
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

            fun bind(notification: NotificationItem) {
                Log.d("Formato de fecha", "Fecha original: ${notification.fecha}")
                fechaTextView.text = notification.fecha
                emailTextView.text = notification.email
            }
        }
    }
}