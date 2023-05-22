package com.example.skybank3

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class Login : AppCompatActivity() {
    private lateinit var edtDUIL: EditText
    private lateinit var edtPINL: EditText
    private lateinit var btnLogL: Button
    private lateinit var btnRegisL: Button

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    // Clase Persona
    data class Persona(val dui: String = "", val pin: String = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        edtDUIL = findViewById(R.id.edtDuiL)
        edtPINL = findViewById(R.id.edtPinL)
        btnLogL = findViewById(R.id.btnloginL)
        btnRegisL = findViewById(R.id.btnRegistrarseL)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        btnLogL.setOnClickListener {
            val cajitaDui = edtDUIL.text.toString()
            val cajitaPin = edtPINL.text.toString()

            if (cajitaDui.isNotEmpty() && cajitaPin.isNotEmpty()) {
                consulta(cajitaDui, cajitaPin)
            } else {
                Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegisL.setOnClickListener {
            val open: Intent = Intent(this, Registrarse::class.java)
            startActivity(open)
        }
    }

    private fun consulta(dui: String, pin: String) {
        val query = databaseReference.child("Users")
            .orderByChild("dui")
            .equalTo(dui)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isLoggedIn = false

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(Persona::class.java)
                    if (user != null && user.pin == pin) {
                        isLoggedIn = true
                        break
                    }
                }

                if (isLoggedIn) {
                    Toast.makeText(this@Login, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val open: Intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(open)
                    finish() // Cerrar la actividad de inicio de sesión para que no se pueda volver atrás
                } else {
                    Toast.makeText(this@Login, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Login, "Error en la consulta", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
