package com.example.weather.Models

 data class City (
     val coord: Coord,
     val country:String,
     val id:Int,
     val name:String,
     val sunrise: Int,
     val sunset: Int,
     val timezone: Int
 )  {
}