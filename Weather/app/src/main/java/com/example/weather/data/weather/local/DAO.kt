package com.example.day1.Room

import androidx.room.*
import com.example.weather.models.MyResponce


@Dao
interface DAO {
    @Query("SELECT * FROM weather")
    suspend fun getAll(): List<MyResponce>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weather: MyResponce)

    @Delete
    suspend fun delete(weather: MyResponce): Int

}