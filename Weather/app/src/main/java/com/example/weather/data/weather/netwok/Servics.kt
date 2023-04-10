package com.example.weather.data.weather.netwok

import com.example.weather.models.MyResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Servics {
    @GET("onecall")
    suspend fun getAllWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("lang") lang: String ,
        @Query("units") units: String
    ): Response<MyResponce>
}
//3b2709ccc4bdcdaac7f7ea5c91b3da94