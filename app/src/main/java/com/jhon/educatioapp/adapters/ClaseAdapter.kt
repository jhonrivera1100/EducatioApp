package com.jhon.educatioapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhon.educatioapp.models.Clase
import com.jhon.educatioapp.R



class ClaseAdapter(private var clases: List<Clase>) : RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_listado_clases, parent, false)
        return ClaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        val clase = clases[position]
        holder.bind(clase)
    }

    override fun getItemCount(): Int {
        return clases.size
    }

    fun updateData(newData: List<Clase>) {
        clases = newData
        notifyDataSetChanged()
    }

    fun setItems(newData: List<Clase>) {
        updateData(newData)
    }

    class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(clase: Clase) {
            itemView.findViewById<TextView>(R.id.tvSubject).text = clase.materia
            itemView.findViewById<TextView>(R.id.tvTopic).text = clase.tema
            itemView.findViewById<TextView>(R.id.tvTime).text = "${clase.horaInicio} - ${clase.horaFin}"
            itemView.findViewById<TextView>(R.id.tvPrice).text = "$${clase.valorClase}"
            itemView.findViewById<TextView>(R.id.tvModality).text = clase.modalidad
        }
    }
}

