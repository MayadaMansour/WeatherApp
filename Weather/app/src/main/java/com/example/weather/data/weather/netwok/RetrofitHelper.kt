package com.example.weather.data.weather.netwok

import com.example.weather.data.weather.netwok.RetrofitHelper.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    }

object API {
    val retrofitService: Servics by lazy {
        retrofit.create(Servics::class.java)
    }
}
