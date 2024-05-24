package com.example.espaciocultural.detail

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.espaciocultural.ModifyActivity
import com.example.espaciocultural.R
import com.example.espaciocultural.ticket.TicketWithNumberActivity
import com.example.espaciocultural.ticket.TicketWithoutNumberActivity
import com.example.espaciocultural.events.BaseEvent
import com.example.espaciocultural.events.DebateEvent
import java.time.format.DateTimeFormatter

class DetailDebateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_debate)

        val numberedSeats = findViewById<CheckBox>(R.id.eventNumCapacity)
        val btnReserve = findViewById<Button>(R.id.btnReserve)
        val btnModify = findViewById<Button>(R.id.btnModify)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val positionEvent = intent.getIntExtra("positionEvent", 0)
        var listEvents = BaseEvent.getEventList(filesDir.toString())
        var event = listEvents[positionEvent] as DebateEvent

        addEventData(event)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            listEvents = BaseEvent.getEventList(filesDir.toString())
            event = listEvents[positionEvent] as DebateEvent
            addEventData(event)
        }

        btnReserve.setOnClickListener()
        {
            if(numberedSeats.isChecked)
            {
                val intent = Intent(this, TicketWithNumberActivity::class.java)
                intent.putExtra("positionEvent", positionEvent)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, TicketWithoutNumberActivity::class.java)
                intent.putExtra("positionEvent", positionEvent)
                startActivity(intent)
            }
        }

        btnModify.setOnClickListener()
        {
            val intent = Intent(this, ModifyActivity::class.java)
            intent.putExtra("positionEvent", positionEvent)
            resultLauncher.launch(intent)
        }

        btnCancel.setOnClickListener()
        {
            finish()
        }

    }

    private fun addEventData(event: DebateEvent)
    {
        val titleEvent = findViewById<TextView>(R.id.eventTitle)
        titleEvent.text = event.name

        val imgEvent = findViewById<ImageView>(R.id.imgEvent)
        val imgPath = filesDir.toString() + "/img/" + event.imageHighRes
        val bitmap = BitmapFactory.decodeFile(imgPath)
        imgEvent.setImageBitmap(bitmap)

        val eventType = findViewById<TextView>(R.id.eventType)
        eventType.text = event.type

        val date = findViewById<TextView>(R.id.eventDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        date.text = formatter.format(event.dateHour)

        val hour = findViewById<TextView>(R.id.eventHour)
        val formatterHour = DateTimeFormatter.ofPattern("HH:mm")
        hour.text = formatterHour.format(event.dateHour)

        val eventLanguage = findViewById<TextView>(R.id.eventLanguage)
        eventLanguage.text = event.language

        val numberedSeats = findViewById<CheckBox>(R.id.eventNumCapacity)
        numberedSeats.isChecked = event.numberedSeats

        val eventMaxCapacity = findViewById<TextView>(R.id.eventMaxCapacity)
        eventMaxCapacity.text = event.maxCapacity.toString()

        val eventPrice = findViewById<TextView>(R.id.eventPrice)
        eventPrice.text = event.ticketPrice.toString()

        val participantsListEvent = findViewById<TextView>(R.id.eventParticipantsList)
        participantsListEvent.text = event.participantList.joinToString(",")
    }
}