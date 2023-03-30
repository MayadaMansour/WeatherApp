package com.example.mvvm.Room

import com.example.weather.models.City
import com.example.weather.models.MyResponce


interface LocalSource {
    suspend fun insertWeathers(city: City)
    suspend fun deleteWeathers(city: City)
    suspend fun getStoreWeathers():List<City>
}