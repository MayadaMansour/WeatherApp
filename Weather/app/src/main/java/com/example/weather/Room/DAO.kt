package com.example.day1.Room

import androidx.room.*
import com.example.weather.Models.Current


@Dao
    interface DAO {
       @Query("SELECT * FROM city")
        suspend fun getAll(): List <Current>
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(current: Current)
       @Delete
        suspend fun delete(current: Current): Int

}