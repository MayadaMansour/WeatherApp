package com.example.mvvm.NetWork

import com.example.weather.Models.Current
import retrofit2.Response


private const val TAG="Weather"
class Client private constructor():RemoteSource{
    val service:Servics by lazy {
        RetrofitHelper.getInstance().create(Servics::class.java)
    }
    override suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String) : Response <Current> {
        val response=service.getAllWeather(lat,lon,exclude,appid)
        return response
    }
    companion object {
        private var myInstance: Client? = null
        fun getInstance():Client? {
            if (myInstance==null)
                myInstance = Client()
            return myInstance
        }
    }



}