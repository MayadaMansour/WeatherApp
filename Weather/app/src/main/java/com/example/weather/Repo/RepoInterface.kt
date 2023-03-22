package com.example.mvvm.Model

import com.example.weather.Models.Current
import retrofit2.Response


interface RepoInterface {
    suspend fun insertWeathers(current: Current)
    suspend fun deleteWeathers(current: Current)
    suspend fun getStoreWeathers():List<Current>
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String):Response<Current>
}