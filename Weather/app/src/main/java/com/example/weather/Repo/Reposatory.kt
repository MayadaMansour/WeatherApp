package com.example.mvvm.Model

import com.example.mvvm.NetWork.RemoteSource
import com.example.mvvm.Room.LocalSource
import com.example.weather.Models.Current
import retrofit2.Response

class Reposatory private constructor(
    var remoteSource: RemoteSource,
    var localSource: LocalSource
):RepoInterface{

    companion object{
        @Volatile
        private var INSTANCE: Reposatory? = null
        fun getInstance (
            remoteSource: RemoteSource,
            localSource: LocalSource
        ): Reposatory {
            return INSTANCE ?: synchronized(this) {
                val temp = Reposatory(
                    remoteSource, localSource)
                INSTANCE = temp
                temp }
        }
    }

    override suspend fun insertWeathers(current: Current) {
        localSource.insertWeathers(current)
    }

    override suspend fun deleteWeathers(current: Current) {
        localSource.deleteWeathers(current)
    }

    override suspend fun getStoreWeathers(): List<Current> {
    return localSource.getStoreWeathers()
    }

    override suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String): Response<Current> {
        return remoteSource.getWeatherOverNetwork(lat,lon,exclude,appid)

    }
}

