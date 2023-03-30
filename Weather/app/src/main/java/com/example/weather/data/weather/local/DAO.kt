package com.example.day1.Room

import androidx.room.*
import com.example.weather.models.City
import com.example.weather.models.MyResponce


@Dao
interface DAO {
    @Query("SELECT * FROM Favorite")
    suspend fun getAll(): List<City>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City)

    @Delete
    suspend fun delete(city: City): Int

}