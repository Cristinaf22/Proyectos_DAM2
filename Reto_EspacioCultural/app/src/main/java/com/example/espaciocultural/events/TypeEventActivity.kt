package com.example.espaciocultural.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.espaciocultural.CreateActivity
import com.example.espaciocultural.R

class TypeEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe_event)

        val textPelicula = findViewById<TextView>(R.id.type_pelicula)
        val textOpera = findViewById<TextView>(R.id.type_concierto)
        val textDebate = findViewById<TextView>(R.id.type_debate)
        val textOtro = findViewById<TextView>(R.id.type_otro)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            finish()
        }

        textPelicula.setOnClickListener() {
            // abrir ventana donde el usuario introduce los datos
            // el boton de la ventana nueva crea el evento y lo guarda en el json
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("EventType", "Película")
            resultLauncher.launch(intent)
        }
        textOpera.setOnClickListener() {
            // abrir ventana donde el usuario introduce los datos
            // el boton de la ventana nueva crea el evento y lo guarda en el json
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("EventType", "Concierto de Ópera")
            resultLauncher.launch(intent)
        }
        textDebate.setOnClickListener() {
            // abrir ventana donde el usuario introduce los datos
            // el boton de la ventana nueva crea el evento y lo guarda en el json
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("EventType", "Debate")
            resultLauncher.launch(intent)
        }
        textOtro.setOnClickListener() {
            // abrir ventana donde el usuario introduce los datos
            // el boton de la ventana nueva crea el evento y lo guarda en el json
            val intent = Intent(this, CreateActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}