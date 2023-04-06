package com.example.weather.ui.alert

import com.example.weather.models.Alert
import com.example.weather.models.City

interface OnClickAlert {
    fun deleteAlerts (alert: Alert)
    fun showAlert (alert: Alert)
}