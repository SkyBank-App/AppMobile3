package com.example.skybank3

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

lateinit var txDUIR: EditText
lateinit var txNacimientoR: EditText
lateinit var txNombreR: EditText
lateinit var txMailR: EditText
lateinit var txTelefonoR: EditText
lateinit var txPINR: EditText
lateinit var btnregsR: Button

var dia: Int = 0
var mes: Int = 0
var ano: Int = 0

private lateinit var firebaseDatabase: FirebaseDatabase
private lateinit var databaseReference: DatabaseReference

class Registrarse : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        txDUIR = findViewById(R.id.edtDUIR)
        txMailR = findViewById(R.id.edtMailR)
        txPINR = findViewById(R.id.edtPINR)
        txNombreR = findViewById(R.id.edtNombreR)
        txNacimientoR = findViewById(R.id.edtFechaNacimientoR)
        txNacimientoR.setOnClickListener(this)
        txTelefonoR = findViewById(R.id.edtTelefonoR)
        txTelefonoR.setText("+503 ")
        btnregsR = findViewById(R.id.btnRegistrarseR)
        btnregsR.setOnClickListener {
            if (txNombreR.text.isEmpty()) {
                txNombreR.error = "Ingrese su nombre completo"
            } else if (txNacimientoR.text.isEmpty()) {
                txNacimientoR.error = "Ingrese su fecha de nacimiento"
            } else if (txDUIR.text.isEmpty()) {
                txDUIR.error = "Ingrese su DUI"
            } else if (txMailR.text.isEmpty()) {
                txMailR.error = "Ingrese su correo electrónico"
            } else if (txTelefonoR.text.isEmpty()) {
                txTelefonoR.error = "Ingrese su número de teléfono"
            } else if (txPINR.text.isEmpty()) {
                txPINR.error = "Ingrese su PIN de seguridad"
            } else {
                subirDatos()
                Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                val open: Intent = Intent(this, Login::class.java)
                startActivity(open)
                return@setOnClickListener
            }
        }

        inicializarFirebase()
    }

    override fun onClick(v: View) {
        if (v == txNacimientoR) {
            val c = Calendar.getInstance()
            dia = c.get(Calendar.DAY_OF_MONTH)
            mes = c.get(Calendar.MONTH)
            ano = c.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    txNacimientoR.setText("$dayOfMonth/${month + 1}/$year")
                },
                dia,
                mes,
                ano
            )
            datePickerDialog.show()
        }
    }

    fun encrypt(password: String): String {
        val bytes = password.toByteArray(StandardCharsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
    }

    private fun subirDatos(): String {
        val nombre = txNombreR.text.toString()
        val fecha = txNacimientoR.text.toString()
        val telefono = txTelefonoR.text.toString()
        val dui = txDUIR.text.toString()
        val mail = txMailR.text.toString()
        val pin = txPINR.text.toString()

        val id = databaseReference.child("Users").push().key

        if (id != null) {
            val persona = Persona(id, nombre, fecha, dui, mail, telefono, pin)
            databaseReference.child("Users").child(dui).setValue(persona)
            Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Error al agregar los datos", Toast.LENGTH_LONG).show()
        }

        return id ?: ""
    }

}

data class Persona(
    val id: String,
    val nombre: String,
    val fechaNacimiento: String,
    val dui: String,
    val mail: String,
    val telefono: String,
    val pin: String
)
