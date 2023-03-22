package com.example.weather.Models

import androidx.room.Entity

@Entity(tableName = "city")

data class MyResponce (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current,
    val hourly: List<Current>,
    val daily: List<Daily>
)