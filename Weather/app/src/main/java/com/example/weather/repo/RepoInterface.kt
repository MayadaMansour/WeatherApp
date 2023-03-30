package com.example.mvvm.Model


import com.example.weather.models.City
import com.example.weather.models.MyResponce


interface RepoInterface {
    suspend fun insertWeathers(city: City)
    suspend fun deleteWeathers(city: City)
    suspend fun getStoreWeathers():List<City>
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String): MyResponce
}