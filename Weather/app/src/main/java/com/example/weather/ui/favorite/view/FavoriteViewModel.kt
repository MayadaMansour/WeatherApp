package com.example.weather.ui.favorite.view

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import com.example.weather.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteViewModel(private val _irepo: RepoInterface) : ViewModel() {
    private var _favoriteWeather: MutableLiveData<List<City>> =
        MutableLiveData<List<City>>()
    val favoriteWeather: LiveData<List<City>> = _favoriteWeather

    init {
        getLocalWeathers()
    }

    fun deleteWeathers(fav: City) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteWeathers(fav)
            getLocalWeathers()
        }
    }

    fun getLocalWeathers() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteWeather.postValue(_irepo.getStoreWeathers())
        }
    }
    fun insertWeathers(fav: City) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertWeathers(fav)
            getLocalWeathers()
        }
    }
}