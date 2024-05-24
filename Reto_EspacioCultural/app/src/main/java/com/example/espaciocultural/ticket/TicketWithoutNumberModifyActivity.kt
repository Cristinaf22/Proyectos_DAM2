package com.example.espaciocultural.ticket

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.espaciocultural.R
import com.example.espaciocultural.events.BaseEvent

class TicketWithoutNumberModifyActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_without_number_modify)

        val positionEvent = intent.getIntExtra("positionEvent", 0)
        val listEvents = BaseEvent.getEventList(filesDir.toString())
        val event = listEvents[positionEvent]

        addEventData(event)

        val btnChecked = findViewById<ImageButton>(R.id.checkReservedTicket)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val userName = findViewById<EditText>(R.id.userName)
        val ticketsToReserved = findViewById<EditText>(R.id.ticketsToReserved)

        btnChecked.setOnClickListener()
        {
            if (event.ticketsBought.containsKey(userName.text.toString())) {
                btnSave.isEnabled = true
                userName.isEnabled = false

                val reservedTickets = findViewById<TextView>(R.id.reservedTickets)
                val currentlyBoughtTickets = event.ticketsBought[userName.text.toString()]

                reservedTickets.text = currentlyBoughtTickets.toString()

                Toast.makeText(this, "Usuario correcto", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "No existe el usuario introducido", Toast.LENGTH_LONG).show()
            }

        }

        btnSave.setOnClickListener()
        {
            val currentlyBoughtTickets = event.ticketsBought[userName.text.toString()]

            if (ticketsToReserved.text.toString().toInt() <= currentlyBoughtTickets!!)
            {
                event.ticketsBought[userName.text.toString()] = currentlyBoughtTickets - ticketsToReserved.text.toString().toInt()

                BaseEvent.updateEvent(event, positionEvent, filesDir.toString())
                Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_LONG).show()
                finish()
            }
            else
            {
                Toast.makeText(this, "Datos introducidos erroneos", Toast.LENGTH_LONG).show()
            }

        }
        btnCancel.setOnClickListener()
        {
            finish()
        }
    }

    private fun addEventData(event: BaseEvent) {
        val titleEvent = findViewById<TextView>(R.id.titleEvent)
        titleEvent.text = event.name

        val imgEvent = findViewById<ImageView>(R.id.imgEvent)
        val imgPath = filesDir.toString() + "/img/" + event.imageHighRes
        val bitmap = BitmapFactory.decodeFile(imgPath)
        imgEvent.setImageBitmap(bitmap)

        val eventMaxCapacity = findViewById<TextView>(R.id.eventMaxCapacity)
        eventMaxCapacity.text = event.maxCapacity.toString()


    }
}