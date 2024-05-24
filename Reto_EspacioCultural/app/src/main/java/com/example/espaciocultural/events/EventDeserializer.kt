package com.example.espaciocultural.events

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class EventDeserializer : JsonDeserializer<List<BaseEvent>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<BaseEvent> {
        val eventList = mutableListOf<BaseEvent>()

        //si el json no existe ni es una lista, retornar la lista vacia
        if (json == null || !json.isJsonArray())
        {
            return eventList
        }

        //https://kotlinlang.org/docs/control-flow.html#for-loops
        //Para cada evento de jsonArray se tiene que buscar el tipo y convertirlo en objeto de ese tipo
        //y agregarlo a la lista
        for (event in json.asJsonArray)
        {
            val typeEvent : BaseEvent?

            when (event.asJsonObject.get("type")?.asString) {
                "Película" -> typeEvent = context?.deserialize<MovieEvent>(event, MovieEvent::class.java)
                "Concierto de Ópera" -> typeEvent = context?.deserialize<OperaConcertEvent>(event, OperaConcertEvent::class.java)
                "Debate" -> typeEvent = context?.deserialize<DebateEvent>(event, DebateEvent::class.java)
                else -> typeEvent = context?.deserialize<BaseEvent>(event, BaseEvent::class.java)
            }

            if (typeEvent != null)
            {
                eventList.add(typeEvent)
            }
        }
        return eventList
    }
}