package com.example.mvvm.Model

import android.content.SharedPreferences
import com.example.mvvm.Room.LocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import com.example.weather.models.City

import com.example.weather.models.MyResponce
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response


class Reposatory private constructor(
    var remoteSource: Client,
    var localSource: LocalSource,
    var sharedPreferences: SharedPreferences
) : RepoInterface {
    lateinit var myResponce: MyResponce
    private var sharedPreferencesedit = sharedPreferences.edit()
    val ALERTSETTINGS = "ALERTSETTINGS"

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
                    remoteSource!!, localSource, sharedPreferences
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

    override  fun getStoreWeathers():  Flow<List<City>> {
        return localSource.getStoreWeathers()

    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        lang: String,
        units: String
    ): Flow<Response<MyResponce>> =
        flow {
            emit(remoteSource.getWeatherOverNetwork(lat, lon, exclude, appid, lang, units))
        }
            .flowOn(Dispatchers.IO)



    override fun getAlerts(): Flow<List<Alert>> {
        return localSource.getAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        localSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        localSource.deleteAlert(alert)
    }

    override fun saveAlertSettings(alertSettings: AlertSettings) {
        sharedPreferencesedit = sharedPreferences.edit()
        sharedPreferencesedit.putString(ALERTSETTINGS, Gson().toJson(alertSettings))
        sharedPreferencesedit.commit()
    }

    override fun getAlertSettings(): AlertSettings? {
        val settingsStr = sharedPreferences.getString(ALERTSETTINGS, null)
        var alertSettings: AlertSettings? = AlertSettings()
        if (settingsStr != null) {
            alertSettings = Gson().fromJson(settingsStr, AlertSettings::class.java)
        }
        return alertSettings
    }

    override fun putStringInSharedPreferences(Key: String, stringInput: String) {
        sharedPreferencesedit.putString(Key, stringInput)
        sharedPreferencesedit.apply()
    }

    override fun getStringInSharedPreferences(Key: String, stringDefault: String): String {
        return sharedPreferences.getString(Key, stringDefault)!!
    }

    override fun putBooleanInSharedPreferences(Key: String, booleanInput: Boolean) {
        sharedPreferencesedit.putBoolean(Key, booleanInput)
        sharedPreferencesedit.apply()
    }

    override fun gutBooleanInSharedPreferences(Key: String, booleanDefualt: Boolean): Boolean {
        return sharedPreferences.getBoolean(Key, booleanDefualt)
    }
}

