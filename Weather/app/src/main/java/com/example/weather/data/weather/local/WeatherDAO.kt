package com.example.day1.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.weather.models.MyResponce


@Database(entities = arrayOf(MyResponce::class), version = 2 )
    abstract class WeatherDAO : RoomDatabase() {
        abstract fun getWeathersDao(): DAO
        companion object{
            @Volatile
            private var INSTANCE: WeatherDAO? = null
            fun getInstance (ctx: Context): WeatherDAO{
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        ctx.applicationContext, WeatherDAO::class.java, "weather2")
                        .build()
                    INSTANCE = instance
                    instance }
            }
        }
    }

