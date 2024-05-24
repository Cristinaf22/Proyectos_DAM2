package com.example.espaciocultural.ticket

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.espaciocultural.R
import com.example.espaciocultural.events.BaseEvent

class TicketWithNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_with_number)

        val btnReserve = findViewById<Button>(R.id.btnReserve)
        val btnModify = findViewById<Button>(R.id.btnModify)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val positionEvent = intent.getIntExtra("positionEvent", 0)
        val listEvents = BaseEvent.getEventList(filesDir.toString())
        val event = listEvents[positionEvent]

        addEventData(event)

        val numberedTickets = mutableListOf<GridViewTicket>()
        val selectedSeats = mutableListOf<Int>()

        //tiene que agregar el asiento y el número de asiento desacuerdo al evento
        for (i in 1..event.maxCapacity) {
            numberedTickets.add(GridViewTicket(i, R.drawable.seat_empty))
        }
        for (selectedSeat in event.ticketLocations)
        {
            numberedTickets[selectedSeat].image = R.drawable.seat_occupied
        }

        val listNumberedTickets = findViewById<GridView>(R.id.ListNumberedTicket)

        val adapter = GridViewTicketAdapter(this,
            R.layout.activity_grid_ticket_with_number, numberedTickets)
        listNumberedTickets.adapter = adapter

        listNumberedTickets.onItemClickListener = AdapterView.OnItemClickListener()
        {
                _, _, seatPosition, _ ->

            if(seatPosition in event.ticketLocations)
            {
                return@OnItemClickListener
            }
            if(seatPosition in selectedSeats)
            {
                numberedTickets[seatPosition].image = R.drawable.seat_empty
                adapter.notifyDataSetChanged()
                selectedSeats.remove(seatPosition)
            }
            else
            {
                numberedTickets[seatPosition].image = R.drawable.seat_selected
                adapter.notifyDataSetChanged()
                selectedSeats.add(seatPosition)
            }
        }

        btnReserve.setOnClickListener()
        {
            event.ticketLocations.addAll(selectedSeats)
            BaseEvent.updateEvent(event, positionEvent, filesDir.toString())
            Toast.makeText(this, "Se ha realizado la reserva correctamente", Toast.LENGTH_LONG).show()
            finish()
        }

        btnModify.setOnClickListener()
        {
            for (selectedSeat in event.ticketLocations) {
                numberedTickets[selectedSeat].image = R.drawable.seat_selected
                adapter.notifyDataSetChanged()
            }
            selectedSeats.addAll(event.ticketLocations)
            event.ticketLocations.clear()
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

    }

}