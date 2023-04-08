package com.example.weather.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.data.weather.LocalSource.Converter
import java.io.Serializable

@Entity(tableName = "Alert")
@TypeConverters(Converter::class)
data class Alert(
    var startDay: Long,
    var endDay: Long,
    var lat: Double,
    var lon: Double,
    @PrimaryKey
    var AlertCityName :String
) : Serializable


