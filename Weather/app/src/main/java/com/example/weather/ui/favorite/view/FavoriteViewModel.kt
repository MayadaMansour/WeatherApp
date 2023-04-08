package com.example.weather.ui.favorite.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.data.weather.netwok.LocalDataState
import com.example.weather.models.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteViewModel(private val _irepo: RepoInterface) : ViewModel() {

    var _favoriteWeather: MutableStateFlow<LocalDataState> =
        MutableStateFlow(LocalDataState.Loading)
    var favoriteWeather: StateFlow<LocalDataState> =_favoriteWeather

    init {
        getLocalWeathers()
    }

    fun deleteWeathers(fav: City) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteWeathers(fav)
            getLocalWeathers()
        }
    }



    fun getLocalWeathers(){
        viewModelScope.launch {
            _irepo.getStoreWeathers().catch {e->_favoriteWeather.value= LocalDataState.Fail(e)  }.collectLatest {
                _favoriteWeather.value=LocalDataState.Success(it)

            }
        }
    }



    fun insertWeathers(fav: City) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertWeathers(fav)
            getLocalWeathers()
        }
    }
}