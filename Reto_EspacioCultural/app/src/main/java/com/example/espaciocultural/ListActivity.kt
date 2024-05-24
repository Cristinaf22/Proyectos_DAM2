package com.example.espaciocultural

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.espaciocultural.detail.DetailActivity
import com.example.espaciocultural.detail.DetailDebateActivity
import com.example.espaciocultural.detail.DetailMovieActivity
import com.example.espaciocultural.detail.DetailOperaConcertActivity
import com.example.espaciocultural.events.BaseEvent
import com.example.espaciocultural.events.GridViewEvent
import com.example.espaciocultural.events.GridViewEventAdapter
import com.example.espaciocultural.events.TypeEventActivity

class ListActivity : AppCompatActivity() {
    private val listEvents = mutableListOf<BaseEvent>()
    private val gridViewEvent = mutableListOf<GridViewEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gridview)

        updateList()
        val gridViewEventList = findViewById<RecyclerView>(R.id.ListEvents)

        val adapter = GridViewEventAdapter(this, gridViewEvent)
        gridViewEventList.hasFixedSize()
        gridViewEventList.layoutManager = GridLayoutManager(this, 3)
        gridViewEventList.adapter = adapter

        val fab = findViewById<View>(R.id.fab)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            updateList()
            adapter.notifyDataSetChanged()
        }

        //hacer un click largo para modificar cada item
        adapter.setOnLongClickListener()
        {

            //depende del click, se abre una activity u otra
            //si seleccionas un evento, se tiene que abrir la activity que corresponde a ese evento
            val positionEvent = gridViewEventList.getChildAdapterPosition(it)
            val intent = Intent(this, ModifyActivity::class.java)
            intent.putExtra("positionEvent", positionEvent)
            resultLauncher.launch(intent)
            true
        }

        adapter.setOnClickListener()
        {
            //depende del click, se abre una activity u otra
            //si seleccionas un evento, se tiene que abrir la activity que corresponde a ese evento
            val positionEvent = gridViewEventList.getChildAdapterPosition(it)
            val event = listEvents[positionEvent]
            val intent = when (event.type) {
                "Película" -> Intent(this, DetailMovieActivity::class.java)
                "Concierto de Ópera" -> Intent(this, DetailOperaConcertActivity::class.java)
                "Debate" -> Intent(this, DetailDebateActivity::class.java)
                else -> Intent(this, DetailActivity::class.java)
            }

            intent.putExtra("positionEvent", positionEvent)
            resultLauncher.launch(intent)
        }

        fab.setOnClickListener {
            val intent = Intent(this, TypeEventActivity::class.java)
            resultLauncher.launch(intent)

        }

    }

    private fun updateList()
    {
        listEvents.clear()
        listEvents.addAll(BaseEvent.getEventList(filesDir.toString()))

        gridViewEvent.clear()

        for (event in listEvents)
        {
            gridViewEvent.add(GridViewEvent(event.imageLowRes, event.name, event.dateHour, event.ticketPrice))
        }
    }
}