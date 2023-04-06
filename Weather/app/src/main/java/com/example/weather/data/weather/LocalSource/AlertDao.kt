package com.example.weather.data.weather.LocalSource

import androidx.room.*
import com.example.weather.models.Alert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDao {
    @Query("SELECT * From Alert")
    fun getAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alerts: Alert)

    @Delete
    suspend fun deleteAlert(alerts: Alert):Int
}