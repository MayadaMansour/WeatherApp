package com.example.weather.localSourceTest

import com.example.mvvm.Room.LocalSource
import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class FakeLocalDataSource():LocalSource {

    var alertList: MutableList<Alert> = mutableListOf()
    var favList: MutableList<City> = mutableListOf()
    override suspend fun insertWeathers(city: City) {
        favList.add(city)
    }

    override suspend fun deleteWeathers(city: City) {
        favList.remove(city)

    }

    override fun getStoreWeathers(): Flow<List<City>>  =flow {
        emit(favList)

    }

    override fun getAlerts(): Flow<List<Alert>> = flow{
        emit(alertList)
    }

    override suspend fun insertAlert(alert: Alert) {
        alertList.add(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertList.remove(alert)

    }


}






