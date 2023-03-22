package com.example.mvvm.NetWork

import com.example.weather.Models.Current
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Servics {
    @GET("onecall")
    suspend fun getAllWeather(
    @Query("lat") lat:Double,
    @Query("lon") lon:Double,
    @Query("exclude") exclude:String,
    @Query("appid") appid:String
    ):Response<Current>
}