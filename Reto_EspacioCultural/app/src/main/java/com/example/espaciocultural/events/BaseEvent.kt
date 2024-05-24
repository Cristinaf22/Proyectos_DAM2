package com.example.espaciocultural.events

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Dictionary


// https://jleehey.github.io/2020/06/08/deserializing-inherited-types-with-gson.html
open class BaseEvent(
    val id: Int,
    val name: String,
    val imageLowRes: String,
    val imageHighRes: String,
    val type: String,
    val dateHour: LocalDateTime,
    val language: String,
    val ticketsBought: MutableMap<String, Int>,
    val ticketLocations: MutableList<Int>,
    val numberedSeats: Boolean, // True si los asientos tienen numero
    val maxCapacity: Int,
    val ticketPrice: Double
): Serializable
{
    companion object {
        private var events = mutableListOf<BaseEvent>()

        private val listEventsType =  object : TypeToken<List<BaseEvent>>() {}.type

        //https://www.baeldung.com/kotlin/kotlinx-serialization-project
        //crea un gsonbuilder(lee/escribe json)
        //registerTypeAdapter define cómo tratar un tipo de objeto
        //JsonDeserializer método que se ejecuta cuando conviertes json en objeto
        private val gson: Gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java,
            JsonDeserializer { json, _, _ ->
                //Convierte un string tipo json en la clase LocalDateTime
                LocalDateTime.parse(json.asJsonPrimitive.asString)
            }).registerTypeAdapter(LocalDateTime::class.java,
            //Corvierte el objeto en string de tipo json
            JsonSerializer<LocalDateTime> { src, _, _ ->
                if(src == null) null
                else JsonPrimitive(src.toString())
            }).registerTypeAdapter(listEventsType, EventDeserializer()).create()

        fun getEventList(fileDir: String): MutableList<BaseEvent>
        {
            if (events.isEmpty())
            {
                val jsonFilePath = "$fileDir/json/datos.json"
                val jsonFile = FileReader(jsonFilePath)

                events = gson.fromJson(jsonFile, listEventsType)
            }
            return events
        }

        fun getNewEventId() : Int {
            return events.size + 1
        }


        // Agrega el evento a la lista de eventos y actualiza el json
        fun addEvent(event : BaseEvent, fileDir: String) {
            events.add(event)
            saveEventList(fileDir)
        }

        fun updateEvent(event : BaseEvent, positionEvent: Int, fileDir: String)
        {
            events[positionEvent] = event
            saveEventList(fileDir)
        }

        fun deleteEvent(positionEvent: Int, fileDir: String)
        {
            events.removeAt(positionEvent)
            saveEventList(fileDir)
        }


        private fun saveEventList(fileDir: String) {
            val jsonFilePath = "$fileDir/json/datos.json"
            File(jsonFilePath).writeText(gson.toJson(events))
        }
    }
}