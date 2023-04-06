package com.example.mvvm.Room

import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import kotlinx.coroutines.flow.Flow


interface LocalSource {
    suspend fun insertWeathers(city: City)
    suspend fun deleteWeathers(city: City)
    suspend fun getStoreWeathers():List<City>



    fun getAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)
}