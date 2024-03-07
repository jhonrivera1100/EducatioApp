import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhon.educatioapp.R
import com.jhon.educatioapp.databinding.ItemListadoClasesBinding
import com.jhon.educatioapp.models.Clase

class ClaseAdapter(private var clases: List<Clase>) : RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder>() {

    inner class ClaseViewHolder(private val binding: ItemListadoClasesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clase: Clase) {
            binding.apply {
                tvSubject.text = clase.materia
                tvTopic.text = clase.tema
                tvTime.text = "${clase.horaInicio} - ${clase.horaFin}"
                tvPrice.text = "$${clase.valorClase}"
                tvModality.text = clase.modalidad
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val binding = ItemListadoClasesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        if (clases.isEmpty()) {
            // Si no hay datos, muestra un elemento de relleno
            holder.bindPlaceholder()
        } else {
            holder.bind(clases[position])
        }
    }

    override fun getItemCount() = if (clases.isEmpty()) 20 else clases.size

    fun updateData(newClases: List<Clase>) {
        clases = newClases
        notifyDataSetChanged()
    }

    private fun ClaseViewHolder.bindPlaceholder() {
        // Acceder directamente a los TextViews del layout sin usar binding
        itemView.findViewById<TextView>(R.id.tvSubject)?.text = "No hay clases disponibles"
        itemView.findViewById<TextView>(R.id.tvTopic)?.text = "ya"
        itemView.findViewById<TextView>(R.id.tvTime)?.text = "tengo"
        itemView.findViewById<TextView>(R.id.tvPrice)?.text = "dolor"
        itemView.findViewById<TextView>(R.id.tvModality)?.text = "de cabeza"
    }

}
