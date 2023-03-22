package com.example.mvvm.NetWork

import com.example.weather.Models.Current



interface NetWorkCallBack {
    fun onSuccessResult(weather:List<Current>)
    fun onFailureResult(errorMsg:String)
}