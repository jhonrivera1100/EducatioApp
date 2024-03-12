package com.jhon.educatioapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jhon.educatioapp.R
import com.jhon.educatioapp.databinding.FragmentSolicitarClaseBinding
import java.util.Calendar
import java.util.Date

class SolicitarFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentSolicitarClaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSolicitarClaseBinding.inflate(inflater, container, false)

        // Obtener el correo electrónico del usuario actualmente autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        // Mostrar el correo electrónico en el TextView
        binding.editTextEmail.setText(email)

        //Definir modalidades
        val modalidad = arrayOf("Virtual", "Presencial")

        // Crear adaptador de modalidad
        val adapterModalidad = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modalidad)
        adapterModalidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establecer el adaptador en el Spinner de modalidad
        binding.spinnerModalidad.adapter = adapterModalidad

        // Definir las materias de bachillerato
        val materias = arrayOf("Matemáticas", "Física", "Química", "Biología", "Historia", "Geografía", "Literatura", "Inglés", "Economía")

        // Crear un adaptador para el Spinner de materias
        val adapterMaterias = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, materias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establecer el adaptador en el Spinner de materias
        binding.spinnerMaterias.adapter = adapterMaterias

        binding.buttonGuardar.setOnClickListener {

            val valorClase = binding.editTextValorClase.text.toString()
            val materia = binding.spinnerMaterias.selectedItem.toString()
            val tema = binding.editTextTema.text.toString()
            val modalidades = binding.spinnerModalidad.selectedItem.toString()
            val fecha = getDateFromDatePicker(binding.datePickerFecha)
            val hora = binding.editTextHora.text.toString()

            // Obtener el correo electrónico
            val email = binding.editTextEmail.text.toString()

            guardarDatosEnFirestore(email, materia, tema, fecha, hora, modalidades, valorClase)
            mostrarPopupSolicitudExitosa()
        }

        return binding.root
    }

    private fun guardarDatosEnFirestore(email: String, materia: String, tema: String, fecha: Date, hora: String, modalidad: String, valorClase: String) {
        val clase = hashMapOf(
            "email" to email,
            "materia" to materia,
            "tema" to tema,
            "fecha" to fecha,
            "hora" to hora,
            "modalidad" to modalidad,
            "valorClase" to valorClase
        )

        // Intenta agregar un documento a la colección "clases"
        db.collection("clases")
            .add(clase)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    // Guardar datos del DatePicker
    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }
    private fun mostrarPopupSolicitudExitosa() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_solicitar)

        val buttonOK = dialog.findViewById<Button>(R.id.buttonOK)
        buttonOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        private const val TAG = "SolicitarFragment"
    }
}
