package com.example.weather.data.weather.LocalSource

import android.content.Context

import com.example.day1.Room.DAO
import com.example.day1.Room.WeatherDAO
import com.example.mvvm.Room.LocalSource
import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import com.example.weather.models.City
import kotlinx.coroutines.flow.Flow


class ConcreteLocalSource(context: Context) : LocalSource {
    private val dao: DAO by lazy {
        val db: WeatherDAO = WeatherDAO.getInstance(context)
        db.getWeathersDao()
    }
    private val alert: AlertDao by lazy {
        val db: WeatherDAO = WeatherDAO.getInstance(context)
        db.getAlertDao()
    }


    override suspend fun insertWeathers(city: City) {
        dao.insert(city)
    }

    override suspend fun deleteWeathers(city: City) {
        dao.delete(city)
    }

    override suspend fun getStoreWeathers(): List<City> {
        return dao.getAll()
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return alert.getAlerts()

    }

    override suspend fun insertAlert(alerts: Alert) {
        alert.insertAlert(alerts)

    }

    override suspend fun deleteAlert(alerts: Alert) {
        alert.deleteAlert(alerts)

    }


}