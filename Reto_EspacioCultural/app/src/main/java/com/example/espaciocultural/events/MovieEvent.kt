package com.example.espaciocultural.events

import java.time.LocalDateTime
import java.util.Date
import java.util.Dictionary

open class MovieEvent(
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
    val directorName : String,
    val premiereYear: Int,
    val mainActors : List<String>,
    val movieLength : Int // Minutos
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