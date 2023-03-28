package com.example.weather.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.models.MyResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteViewModel( private val _irepo: RepoInterface) :ViewModel(){
    private var _product : MutableLiveData<List<MyResponce>> = MutableLiveData<List<MyResponce>>()
    val products: LiveData<List<MyResponce>> = _product
    init {
        getLocalWeathers()
    }


    fun deleteWeathers(weather: MyResponce){
        viewModelScope.launch (Dispatchers.IO){
            _irepo.deleteWeathers(weather)
            getLocalWeathers()
        }
    }

    private fun getLocalWeathers() {
        viewModelScope.launch (Dispatchers.IO){
            _product.postValue(_irepo.getStoreWeathers())

        }
    }
}