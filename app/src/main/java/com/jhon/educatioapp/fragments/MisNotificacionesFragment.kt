import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.jhon.educatioapp.R

class MisNotificacionesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el diseño del elemento aquí
        val view = inflater.inflate(R.layout.item_notificaciones, container, false)

        // Obtener referencia del botón de cierre
        val closeButton = view.findViewById<ImageView>(R.id.closeButton)

        // Establecer un listener para el botón de cierre
        closeButton.setOnClickListener {
            // Cerrar el layout
            val relativeLayout = view.findViewById<RelativeLayout>(R.id.relativeLayout)
            relativeLayout.visibility = View.GONE
        }

        return view
    }
}
