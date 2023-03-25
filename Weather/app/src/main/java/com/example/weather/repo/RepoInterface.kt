package com.example.mvvm.Model


import com.example.weather.models.MyResponce


interface RepoInterface {
    suspend fun insertWeathers(current: MyResponce)
    suspend fun deleteWeathers(current: MyResponce)
    suspend fun getStoreWeathers():List<MyResponce>
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String): MyResponce
}