package com.example.skybank3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadScreen : AppCompatActivity() {

    private val Tiempo = 1800
    private  val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screen)

        handler.postDelayed({
            val intent: Intent = Intent(this,Login::class.java)
            startActivity(intent)
        },Tiempo.toLong())

    }
}