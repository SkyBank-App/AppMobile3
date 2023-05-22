package com.example.skybank3.ui.slideshow.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skybank3.databinding.FragmentGalleryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

lateinit var nombre: EditText
lateinit var duii: EditText

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize EditText fields
        nombre = binding.edtNombreR2
        duii = binding.edtDUIR2

        // Initialize Firebase
        inicializarFirebase()

        // Save button click listener
        binding.btnloginL4.setOnClickListener {
            saveCitaToFirebase()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
    }

    private fun saveCitaToFirebase() {
        val citasRef = databaseReference.child("Citas")
        val citaId = citasRef.push().key ?: ""
        val cita = Cita(citaId, nombre.text.toString(), duii.text.toString(), "Asunto de la cita")
        citasRef.child(citaId).setValue(cita)

        // Show a toast or perform any other action to indicate successful save
    }

}

data class Cita(
    val id: String,
    val nombre: String,
    val dui: String,
    val asunto: String
)
