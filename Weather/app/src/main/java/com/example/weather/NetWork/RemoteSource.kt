package com.example.mvvm.NetWork

import com.example.weather.Models.Current
import retrofit2.Response


interface RemoteSource {
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String):Response<Current>
}