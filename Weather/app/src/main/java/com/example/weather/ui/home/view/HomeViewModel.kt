package com.example.weather.ui.home.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.models.MyResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepoInterface) : ViewModel() {
    private var _currentWeather = MutableLiveData<MyResponce>()
    var currentWeather: LiveData<MyResponce> = _currentWeather

    fun getWeatherFromApi(lat: Double, lon: Double, exclude: String, appid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repo.getWeatherOverNetwork(lat, lon, exclude, appid))
        }
    }
}


