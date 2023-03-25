package com.example.mvvm.Model

import com.example.weather.data.weather.netwok.Client

import com.example.weather.models.MyResponce


class Reposatory private constructor(

    var remoteSource: Client?

    //var localSource: LocalSource
):RepoInterface {
     lateinit var myResponce:MyResponce
    companion object{
        @Volatile
        private var INSTANCE: Reposatory? = null
        fun getInstance (
            remoteSource: Client?
            //localSource: LocalSource
        ): Reposatory {
            return INSTANCE ?: synchronized(this) {
                val temp = Reposatory(
                    remoteSource)
                INSTANCE = temp
                temp }
        }
    }



    override suspend fun insertWeathers(current: MyResponce) {
        //    localSource.insertWeathers(current)
    }

    override suspend fun deleteWeathers(current: MyResponce) {
        //   localSource.deleteWeathers(current)
    }

    override suspend fun getStoreWeathers(): List<MyResponce> {
   // return localSource.getStoreWeathers()
        return listOf()
    }

    override suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String): MyResponce {

        val response = remoteSource?.getWeatherOverNetwork(lat, lon, exclude, appid)
        if (response?.isSuccessful == true) {
            response.body()!!.also { myResponce = it }
        }
    return myResponce
}


}

