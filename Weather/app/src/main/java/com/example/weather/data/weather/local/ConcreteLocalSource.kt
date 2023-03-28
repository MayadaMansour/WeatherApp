package com.example.weather.data.weather.local

import android.content.Context

import com.example.day1.Room.DAO
import com.example.day1.Room.WeatherDAO
import com.example.mvvm.Room.LocalSource
import com.example.weather.models.MyResponce


class ConcreteLocalSource(context:Context): LocalSource {
    private val dao: DAO by lazy {
        val db:WeatherDAO= WeatherDAO.getInstance(context)
        db.getWeathersDao()
    }

    override suspend fun insertWeathers(current: MyResponce) {
        dao.insert(current)
    }

    override suspend fun deleteWeathers(current: MyResponce) {
        dao.delete(current)
    }

    override suspend fun getStoreWeathers(): List<MyResponce> {
        return dao.getAll()
    }
}