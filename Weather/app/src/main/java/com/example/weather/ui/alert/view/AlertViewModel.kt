package com.example.weather.ui.alert.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.data.weather.LocalSource.RoomState
import com.example.weather.data.weather.netwok.ApiState
import com.example.weather.data.weather.netwok.LocalDataStateAlerts
import com.example.weather.models.Alert
import com.example.weather.models.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlertViewModel(private val repo: RepoInterface) : ViewModel() {
   var _currentAlert: MutableStateFlow<LocalDataStateAlerts> = MutableStateFlow(LocalDataStateAlerts.Loading)
    var currentAlert: StateFlow<LocalDataStateAlerts> = _currentAlert


    init {
        getAlertsDB()
    }

    fun deleteAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
            getAlertsDB()
        }
    }


    fun getAlertsDB(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAlerts()
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _currentAlert.value=LocalDataStateAlerts.Fail(it)
                    }
                    .collect {
                        _currentAlert.value = LocalDataStateAlerts.Success(it)

                    }
            }
        }
    }

    fun insertAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlert(alert)
            getAlertsDB()
        }
    }


}
