package com.example.weather.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.data.weather.local.Converter

@Entity(tableName = "Favorite")
@TypeConverters(Converter::class)
data class City (
    var citylat:Double,
    var citylon:Double,
    @PrimaryKey
    var city:String
)  :java.io.Serializable