package com.jhon.educatioapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listado_solicitud.misclasesdocente.ListadoClases
import com.jhon.educatioapp.R
import com.squareup.picasso.Picasso

class ListadoClasesAdapter(private val listadoClasesList: List<ListadoClases>) : RecyclerView.Adapter<ListadoClasesAdapter.ListadoClasesViewHolder>() {

    class ListadoClasesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvListadoClasesName: TextView = view.findViewById(R.id.tvListadoClasesName)
        val ivListadoClases : ImageView = view.findViewById(R.id.ivListadoClases)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListadoClasesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listado_clases, parent, false)
        return ListadoClasesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListadoClasesViewHolder, position: Int) {
        val pokemon = listadoClasesList[position]
        Picasso.get().load("https://cdn-icons-png.flaticon.com/512/3135/3135715.png").into(holder.ivListadoClases)
        holder.tvListadoClasesName.text = pokemon.name
    }

    override fun getItemCount() = listadoClasesList.size
}
