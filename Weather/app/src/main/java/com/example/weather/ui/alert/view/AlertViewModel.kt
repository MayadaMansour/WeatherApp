package com.example.weather.ui.alert.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.RepoInterface
import com.example.weather.data.weather.netwok.LocalDataStateAlerts
import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertViewModel(private val repo: RepoInterface) : ViewModel() {
    var _currentAlert: MutableStateFlow<LocalDataStateAlerts> =
        MutableStateFlow(LocalDataStateAlerts.Loading)
    var currentAlert: StateFlow<LocalDataStateAlerts> = _currentAlert

    fun deleteAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
            getAlertsDB()
        }
    }

    fun getAlertsDB() {
        viewModelScope.launch {
            repo.getAlerts().catch { e -> _currentAlert.value = LocalDataStateAlerts.Fail(e) }
                .collectLatest {
                    _currentAlert.value = LocalDataStateAlerts.Success(it)
                }
        }

    }

    fun insertAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlert(alert)
            getAlertsDB()
        }
    }

    fun getAlertSettings(): AlertSettings? {
        return repo.getAlertSettings()
    }

    fun saveAlertSettings(alertSettings: AlertSettings) {
        repo.saveAlertSettings(alertSettings)
    }

}
