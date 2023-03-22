package com.example.weather.Models

import android.graphics.drawable.Icon

data class Weather (
 val id: Long,
 val main: Main,
 val description: Description,
 val icon: Icon
)
