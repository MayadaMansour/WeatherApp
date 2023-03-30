package com.example.weather.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.data.weather.local.Converter


//@Entity(tableName = "weather")
@TypeConverters(Converter::class)
data class MyResponce (
    @PrimaryKey val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: CurrentWeather,
    val hourly: List<CurrentWeather>,
    val daily: List<Daily>?,
   // var isFavorite:Boolean?,
 //   val minutely: List<Minutely>?=null,
   // val alerts: List<Alert>?=null,
   // var countryName:String?=null
)
