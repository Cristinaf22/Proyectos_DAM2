package com.example.espaciocultural.ticket

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.espaciocultural.R
import com.example.espaciocultural.events.BaseEvent

class TicketWithoutNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_without_number)

        val btnReserve = findViewById<Button>(R.id.btnReserve)
        val btnModify = findViewById<Button>(R.id.btnModify)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val positionEvent = intent.getIntExtra("positionEvent", 0)
        var listEvents = BaseEvent.getEventList(filesDir.toString())
        var event = listEvents[positionEvent]

        addEventData(event)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            listEvents = BaseEvent.getEventList(filesDir.toString())
            event = listEvents[positionEvent]
            addEventData(event)
        }

        btnReserve.setOnClickListener()
        {
            val userNameEditText = findViewById<EditText>(R.id.userName)
            val reservedTicketEditText = findViewById<EditText>(R.id.ticketsToReserved)
            val username = userNameEditText.text.toString()
            var reservedTickets = reservedTicketEditText.text.toString().toInt()

            var totalTicketsBought = 0
            for(ticketsBought in event.ticketsBought.values) {
                totalTicketsBought = totalTicketsBought + ticketsBought
            }

            if (totalTicketsBought + reservedTickets > event.maxCapacity) {
                Toast.makeText(this, "No hay suficientes entradas disponibles", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(event.ticketsBought.containsKey(username)){
                reservedTickets = reservedTickets + event.ticketsBought[username]!!
            }

            event.ticketsBought[username] = reservedTickets

            BaseEvent.updateEvent(event, positionEvent, filesDir.toString())
            Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_LONG).show()
            finish()
        }

        btnModify.setOnClickListener()
        {

            val intent = Intent(this, TicketWithoutNumberModifyActivity::class.java)
            intent.putExtra("positionEvent", positionEvent)
            resultLauncher.launch(intent)
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

        val reservedTickets = findViewById<TextView>(R.id.reservedTickets)
        var totalTicketsBought = 0
        for(ticketsBought in event.ticketsBought.values) {
            totalTicketsBought = totalTicketsBought + ticketsBought
        }
        reservedTickets.text = totalTicketsBought.toString()

    }
}