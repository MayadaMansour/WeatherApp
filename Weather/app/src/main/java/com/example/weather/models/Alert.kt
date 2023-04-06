package com.example.weather.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.data.weather.LocalSource.Converter

@Entity(tableName = "Alert")
@TypeConverters(Converter::class)
data class Alert(
    var Time: Long,
    var startDay: Long,
    var endDay: Long,
    var lat: Double,
    var lon: Double,
    @PrimaryKey
    var AlertCityName :String
) {

}