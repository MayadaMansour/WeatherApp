package com.example.weather.localSourceTest

import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.data.weather.netwok.RetrofitHelper
import com.example.weather.data.weather.netwok.Servics
import com.example.weather.models.MyResponce
import retrofit2.Response

class FakeRemoteDataSource() : RemoteSourceInterface {
    lateinit var weatherResponse: MyResponce

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        lang: String,
        units: String
    ): Response<MyResponce> {
        return Response.success(weatherResponse)
    }
}





