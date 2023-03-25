package com.example.weather.data.weather.netwok

import com.example.weather.models.CurrentWeather
import com.example.weather.models.MyResponce


interface NetWorkCallBack {
    fun onSuccessResult(weather:List<MyResponce>)
    fun onFailureResult(errorMsg:String)
}