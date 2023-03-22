package com.example.mvvm.Room

import com.example.weather.Models.Current

interface LocalSource {
    suspend fun insertWeathers(current: Current)
    suspend fun deleteWeathers(current: Current)
    suspend fun getStoreWeathers():List<Current>
}