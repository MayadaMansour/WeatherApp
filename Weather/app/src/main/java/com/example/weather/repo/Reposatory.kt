package com.example.mvvm.Model

import com.example.mvvm.Room.LocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.City

import com.example.weather.models.MyResponce


class Reposatory private constructor(
    var remoteSource: Client,
    var localSource: LocalSource
) : RepoInterface {
    lateinit var myResponce: MyResponce

    companion object {
        @Volatile
        private var INSTANCE: Reposatory? = null
        fun getInstance(
            remoteSource: Client?,
            localSource: LocalSource
        ): Reposatory {
            return INSTANCE ?: synchronized(this) {
                val temp = Reposatory(
                    remoteSource!!, localSource
                )
                INSTANCE = temp
                temp
            }
        }

    }

    override suspend fun insertWeathers(city: City) {
        localSource.insertWeathers(city)
    }

    override suspend fun deleteWeathers(city: City) {
        localSource.deleteWeathers(city)
    }

    override suspend fun getStoreWeathers(): List<City> {
        return localSource.getStoreWeathers()

    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String
    ): MyResponce {
        val response = remoteSource.getWeatherOverNetwork(lat, lon, exclude, appid)
        if (response.isSuccessful == true) {
            response.body()!!.also { myResponce = it }
        }
        return myResponce
    }
}

