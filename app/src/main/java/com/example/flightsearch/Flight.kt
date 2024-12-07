package com.example.flightsearch

data class Flight(
    val departure: String,
    val arrival: String,
    val departureName: String,
    val arrivalName: String,
    var isFavorite: Boolean = false
)
