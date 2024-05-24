package com.example.espaciocultural.events

import java.time.LocalDateTime
import java.util.Date
import java.util.Dictionary

class DebateEvent(
    id: Int,
    name: String,
    imageLowRes: String,
    imageHighRes: String,
    type: String,
    dateHour: LocalDateTime,
    language: String,
    ticketsBought: MutableMap<String, Int>,
    ticketLocations: MutableList<Int>,
    numberedSeats: Boolean,
    maxCapacity: Int,
    ticketPrice: Double,
    val participantList : List<String>,
) : BaseEvent(
    id,
    name,
    imageLowRes,
    imageHighRes,
    type,
    dateHour,
    language,
    ticketsBought,
    ticketLocations,
    numberedSeats,
    maxCapacity,
    ticketPrice)