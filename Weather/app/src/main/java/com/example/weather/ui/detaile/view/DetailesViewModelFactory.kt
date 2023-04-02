package com.example.weather.ui.detaile.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.Model.Reposatory
import com.example.weather.ui.home.view.HomeViewModel


class DetailesViewModelFactory(private val repo: Reposatory): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(DetailesViewModel::class.java)){
            DetailesViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}