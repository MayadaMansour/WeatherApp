package com.example.weather.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.Model.Reposatory


class FavoriteViewModelFactory(private val repo: Reposatory): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class not Found")
        }
    }
}