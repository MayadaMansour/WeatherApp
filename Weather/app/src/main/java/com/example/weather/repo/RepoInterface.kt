package com.example.mvvm.Model


import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface RepoInterface {
    suspend fun insertWeathers(city: City)
    suspend fun deleteWeathers(city: City)
    suspend fun getStoreWeathers():List<City>
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,exclude:String,appid:String,lang: String,
                                      units: String): Flow<Response<MyResponce>>


    fun getAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

    fun saveAlertSettings(alertSettings: AlertSettings)
    fun getAlertSettings(): AlertSettings?

    fun putStringInSharedPreferences(Key:String,stringInput:String)
    fun getStringInSharedPreferences(Key:String,stringDefault:String):String
    fun putBooleanInSharedPreferences(Key:String,booleanInput:Boolean)
    fun gutBooleanInSharedPreferences(Key:String,booleanDefualt:Boolean):Boolean
}