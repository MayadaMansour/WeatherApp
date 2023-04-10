package com.example.weather.ui.home.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.data.weather.netwok.ApiState
import com.example.weather.models.MyResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(private val repo: RepoInterface) : ViewModel() {

    var _currentWeather: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    var currentWeather: StateFlow<ApiState> = _currentWeather


    fun getWeatherFromApi(latitude: Double, longitude: Double, exclude: String, appid: String,lang:String,units:String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherOverNetwork(latitude, longitude, exclude, appid,lang,units)
            withContext(Dispatchers.Main) {
                response
                    .catch { e -> _currentWeather.value = ApiState.Fail(e) }
                    .collectLatest {
                        _currentWeather.value = ApiState.Success(it)
                    }
            }
        }
    }


}



