package com.example.weather.data.weather.netwok

import com.example.weather.models.MyResponce
import retrofit2.Response

class Client private constructor(): RemoteSourceInterface {
    val service: Servics by lazy {
        RetrofitHelper.getInstance().create(Servics::class.java)
    }
    override suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String) : Response<MyResponce> {
        val response=service.getAllWeather(57.7,12.2,"huorly","3b2709ccc4bdcdaac7f7ea5c91b3da94")
        return response
    }
    companion object {
        private var myInstance: Client? = null
        fun getInstance(): Client? {
            if (myInstance==null)
                myInstance = Client()
            return myInstance
        }
    }



}