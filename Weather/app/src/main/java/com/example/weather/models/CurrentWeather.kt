package com.example.weather.models

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity

@Entity(tableName = "city")
data class CurrentWeather (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: String,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val wind_speed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val pop: Double? = null,
    val rain: Rain? = null,
)  {}
