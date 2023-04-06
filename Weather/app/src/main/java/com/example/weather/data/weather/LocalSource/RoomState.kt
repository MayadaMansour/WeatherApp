package com.example.weather.data.weather.LocalSource

import com.example.weather.models.Alert


sealed class RoomState{
    class Success (val data: List<Alert>): RoomState()
    class Failure(val msg:Throwable): RoomState()
    object Loading: RoomState()
}