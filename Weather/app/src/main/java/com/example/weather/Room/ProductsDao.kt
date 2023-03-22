package com.example.day1.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.Models.Current


@Database(entities = arrayOf(Current::class), version = 1 )

    abstract class ProductsDao : RoomDatabase() {
        abstract fun getProductsDao(): DAO
        companion object{
            @Volatile
            private var INSTANCE: ProductsDao? = null
            fun getInstance (ctx: Context): ProductsDao{
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        ctx.applicationContext, ProductsDao::class.java, "newWeather")
                        .build()
                    INSTANCE = instance
                    instance }
            }
        }
    }

