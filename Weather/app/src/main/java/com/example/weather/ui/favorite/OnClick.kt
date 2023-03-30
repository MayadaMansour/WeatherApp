package com.example.weather.ui.favorite

import com.example.weather.models.City
import com.example.weather.models.MyResponce

interface OnClick {
    fun sendData(weather:MyResponce)
    fun deleteWeathers (city: City)
}