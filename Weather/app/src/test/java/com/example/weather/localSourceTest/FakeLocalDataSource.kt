package com.example.weather.localSourceTest

import com.example.mvvm.Room.LocalSource
import com.example.weather.models.Alert
import com.example.weather.models.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf



class FakeLocalDataSource(
    private var alertList: MutableList<Alert> = mutableListOf(),
    private var localSource: MutableList<City> = mutableListOf()
):LocalSource {
    override suspend fun insertWeathers(city: City) {
        localSource.add(city)
    }

    override suspend fun deleteWeathers(city: City) {
        localSource.remove(city)

    }

    override fun getStoreWeathers(): Flow<List<City>> {
        return flowOf(localSource)

    }

    override fun getAlerts(): Flow<List<Alert>> {
        return flowOf(alertList)
    }

    override suspend fun insertAlert(alert: Alert) {
        alertList.add(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertList.remove(alert)
    }


}