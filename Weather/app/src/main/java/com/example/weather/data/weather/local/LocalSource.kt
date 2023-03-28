package com.example.mvvm.Room

import com.example.weather.models.MyResponce


interface LocalSource {
    suspend fun insertWeathers(current: MyResponce)
    suspend fun deleteWeathers(current: MyResponce)
    suspend fun getStoreWeathers():List<MyResponce>
}