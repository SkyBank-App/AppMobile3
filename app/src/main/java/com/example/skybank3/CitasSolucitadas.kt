package com.example.skybank3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CitasSolucitadas : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: CitasAdapter

    data class Cita(
        val id: String = "",
        val nombre: String = "",
        val dui: String = "",
        val asunto: String = ""
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_citas_solucitadas, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.RecyclerCita)
        adapter = CitasAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("Citas")
        fetchCitasFromFirebase()

        return root
    }

    private fun fetchCitasFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val citasList = mutableListOf<Cita>()

                for (citaSnapshot in snapshot.children) {
                    val cita = citaSnapshot.getValue(Cita::class.java)
                    cita?.let {
                        citasList.add(it)
                    }
                }

                adapter.submitList(citasList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error case
            }
        })
    }

    inner class CitasAdapter : RecyclerView.Adapter<CitasAdapter.CitasViewHolder>() {
        private var citasList: List<Cita> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitasViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_citasolicitadas, parent, false)
            return CitasViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CitasViewHolder, position: Int) {
            val cita = citasList[position]
            holder.bind(cita)
        }

        override fun getItemCount(): Int {
            return citasList.size
        }

        fun submitList(list: List<Cita>) {
            citasList = list
            notifyDataSetChanged()
        }

        inner class CitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombre)
            private val duiTextView: TextView = itemView.findViewById(R.id.textViewDui)
            private val asuntoTextView: TextView = itemView.findViewById(R.id.textViewAsunto)

            fun bind(cita: Cita) {
                nombreTextView.text = cita.nombre
                duiTextView.text = cita.dui
                asuntoTextView.text = cita.asunto
            }
        }
    }

    companion object {
        fun newInstance() = CitasSolucitadas()
    }
}
