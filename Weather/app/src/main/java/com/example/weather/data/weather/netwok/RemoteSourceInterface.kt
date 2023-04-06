package com.example.weather.data.weather.netwok

import com.example.weather.models.MyResponce
import retrofit2.Response
import retrofit2.http.Query


interface RemoteSourceInterface {
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String,lang: String,
                                       units: String):Response<MyResponce>
}