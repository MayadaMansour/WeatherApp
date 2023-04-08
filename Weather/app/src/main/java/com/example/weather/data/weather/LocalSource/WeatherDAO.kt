package com.example.day1.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.data.weather.LocalSource.AlertDao
import com.example.weather.models.Alert
import com.example.weather.models.City

import com.example.weather.models.MyResponce


@Database(entities = arrayOf(City::class, Alert::class, MyResponce::class), version = 4)
abstract class WeatherDAO : RoomDatabase() {
    abstract fun getWeathersDao(): DAO
    abstract fun getAlertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDAO? = null
        fun getInstance(ctx: Context): WeatherDAO {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDAO::class.java, "weather5"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

