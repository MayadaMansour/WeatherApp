package com.example.weather.localSourceTest

import android.content.SharedPreferences
import com.example.mvvm.Model.RepoInterface
import com.example.mvvm.Room.LocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class FakeReposatory
 : RepoInterface {
    var fav:MutableList<City> = mutableListOf()
    var ala:MutableList<Alert> = mutableListOf()
    override suspend fun insertWeathers(city: City) {
        fav.add(city)
    }

    override suspend fun deleteWeathers(city: City) {
       fav.remove(city)
    }

    override fun getStoreWeathers(): Flow<List<City>> {
        return flow {
            val list = fav.toList()
            emit(list)
        }

    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        lang: String,
        units: String
    ): Flow<Response<MyResponce>> {
        TODO("Not yet implemented")
    }

    override fun getAlerts(): Flow<List<Alert>> = flow{
        val list = ala.toList()
        emit(list)
    }

    override suspend fun insertAlert(alert: Alert) {
        ala.add(alert)

    }

    override suspend fun deleteAlert(alert: Alert) {
        ala.remove(alert)
    }

    override fun saveAlertSettings(alertSettings: AlertSettings) {
        TODO("Not yet implemented")
    }

    override fun getAlertSettings(): AlertSettings? {
        TODO("Not yet implemented")
    }

    override fun putStringInSharedPreferences(Key: String, stringInput: String) {
        TODO("Not yet implemented")
    }

    override fun getStringInSharedPreferences(Key: String, stringDefault: String): String {
        TODO("Not yet implemented")
    }

    override fun putBooleanInSharedPreferences(Key: String, booleanInput: Boolean) {
        TODO("Not yet implemented")
    }

    override fun gutBooleanInSharedPreferences(Key: String, booleanDefualt: Boolean): Boolean {
        TODO("Not yet implemented")
    }
}