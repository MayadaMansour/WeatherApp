package com.example.weather.data.weather.netwok

import com.example.weather.models.MyResponce
import retrofit2.Response

class Client private constructor(): RemoteSourceInterface {
    val service: Servics by lazy {
        RetrofitHelper.getInstance().create(Servics::class.java)
    }
    override suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String,lang:String,units:String) : Response<MyResponce> {
        val response=service.getAllWeather(lat,lon,exclude,appid,lang,units)
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