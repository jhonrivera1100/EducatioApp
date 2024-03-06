package com.jhon.educatioapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jhon.educatioapp.R
import java.util.*

class SolicitarFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_solicitar__clase, container, false)

        val textEmail = rootView.findViewById<EditText>(R.id.editTextEmail)
        val textPassword = rootView.findViewById<EditText>(R.id.editTextPassword)
        val spinnerMaterias: Spinner = rootView.findViewById(R.id.spinnerMaterias)
        val temaEditText = rootView.findViewById<EditText>(R.id.editTextTema)
        val fechaDatePicker = rootView.findViewById<DatePicker>(R.id.datePickerFecha)
        val horaEditText = rootView.findViewById<EditText>(R.id.editTextHora)
        val botonGuardar = rootView.findViewById<Button>(R.id.buttonGuardar)
        val spinnerModalidad: Spinner = rootView.findViewById(R.id.spinnerModalidad)

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        //Definir modalidades
        val modalidad = arrayOf("Virtual", "Presencial")

        // Crear adaptador de modalidad
        val adapterModalidad = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modalidad)
        adapterModalidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establecer el adaptador en el Spinner de modalidad
        spinnerModalidad.adapter = adapterModalidad

        // Definir las materias de bachillerato
        val materias = arrayOf("Matemáticas", "Física", "Química", "Biología", "Historia", "Geografía", "Literatura", "Inglés", "Economía")

        // Crear un adaptador para el Spinner de materias
        val adapterMaterias = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, materias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establecer el adaptador en el Spinner de materias
        spinnerMaterias.adapter = adapterMaterias

        botonGuardar.setOnClickListener {
            val materia = spinnerMaterias.selectedItem.toString()
            val tema = temaEditText.text.toString()
            val modalidad = spinnerModalidad.selectedItem.toString()
            val fecha = getDateFromDatePicker(fechaDatePicker)
            val hora = horaEditText.text.toString()

            // Obtener el correo electrónico y la contraseña ingresados por el usuario
            val email = textEmail.text.toString()
            val password = textPassword.text.toString()

            // Autenticar al usuario con correo electrónico y contraseña
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        // Una vez autenticado el usuario, guardar los datos en Firestore
                        guardarDatosEnFirestore(materia, tema, fecha, hora, modalidad)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                    }
                }
        }

        return rootView
    }

    private fun guardarDatosEnFirestore(materia: String, tema: String, fecha: Date, hora: String, modalidad: String) {
        val clase = hashMapOf(
            "materia" to materia,
            "tema" to tema,
            "fecha" to fecha,
            "hora" to hora,
            "modalidad" to modalidad
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

    companion object {
        private const val TAG = "SolicitarFragment"
    }
}
