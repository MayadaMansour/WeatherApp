package com.example.weather.ui.favorite

import com.example.weather.models.City
import com.example.weather.models.MyResponce

interface OnClick {
    fun sendData(lat:Double,lon:Double)
    fun deleteWeathers (city: City)
}