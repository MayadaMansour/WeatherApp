package com.example.mvvm.Model

import android.content.SharedPreferences
import androidx.compose.runtime.key
import com.example.mvvm.Room.LocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.Alert
import com.example.weather.models.City

import com.example.weather.models.MyResponce
import kotlinx.coroutines.flow.Flow


class Reposatory private constructor(
    var remoteSource: Client,
    var localSource: LocalSource,
    var sharedPreferences: SharedPreferences
) : RepoInterface {
    lateinit var myResponce: MyResponce
    private val sharedPreferencesedit= sharedPreferences.edit()

    companion object {
        @Volatile
        private var INSTANCE: Reposatory? = null
        fun getInstance(
            remoteSource: Client?,
            localSource: LocalSource,
            sharedPreferences: SharedPreferences
        ): Reposatory {
            return INSTANCE ?: synchronized(this) {
                val temp = Reposatory(
                    remoteSource!!, localSource,sharedPreferences
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
        appid: String,
        lang:String,
        units:String
    ): MyResponce {
        val response = remoteSource.getWeatherOverNetwork(lat, lon, exclude, appid,lang,units)
        if (response.isSuccessful == true) {
            response.body()!!.also { myResponce = it }
        }
        return myResponce
    }


    override fun getAlerts(): Flow<List<Alert>> {
         return localSource.getAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        localSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        localSource.deleteAlert(alert)
    }

    override fun putStringInSharedPreferences(Key: String, stringInput: String) {
        sharedPreferencesedit.putString(Key,stringInput)
        sharedPreferencesedit.apply()
    }

    override fun getStringInSharedPreferences(Key: String, stringDefault: String): String {
       return sharedPreferences.getString(Key,stringDefault)!!
    }

    override fun putBooleanInSharedPreferences(Key: String, booleanInput: Boolean) {
        sharedPreferencesedit.putBoolean(Key,booleanInput)
        sharedPreferencesedit.apply()
    }

    override fun gutBooleanInSharedPreferences(Key: String, booleanDefualt: Boolean): Boolean {
        return sharedPreferences.getBoolean(Key,booleanDefualt)
    }
}

