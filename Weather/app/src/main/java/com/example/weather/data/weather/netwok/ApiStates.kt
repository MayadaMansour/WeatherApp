package com.example.weather.data.weather.netwok

import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import retrofit2.Response

sealed class  ApiState {
    class Success(var data: Response<MyResponce>):ApiState()
    class Fail(val msg : Throwable):ApiState()
    object Loading :ApiState()
}
sealed class  LocalDataState {
    class Success(var data: List<City>):LocalDataState()
    class Fail(val msg : Throwable):LocalDataState()
    object Loading :LocalDataState()
}
sealed class  LocalDataStateAlerts {
    class Success(var data: List<Alert>?):LocalDataStateAlerts()
    class Fail(val msg : Throwable):LocalDataStateAlerts()
    object Loading :LocalDataStateAlerts()
}