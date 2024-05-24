package com.example.espaciocultural.events

import java.time.LocalDateTime
import java.util.Dictionary

class OperaConcertEvent(
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
    val songwriter : String,
    val principalSoloist: List<String>,
    val chorusName : String,
    val orchestraName : String
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