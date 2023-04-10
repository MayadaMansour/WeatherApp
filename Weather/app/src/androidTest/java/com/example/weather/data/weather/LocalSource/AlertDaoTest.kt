package com.example.weather.data.weather.LocalSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.day1.Room.DAO
import com.example.day1.Room.WeatherDAO
import com.example.weather.models.Alert
import com.example.weather.models.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import kotlinx.coroutines.test.runBlockingTest





@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: WeatherDAO
    private lateinit var alertDao: AlertDao

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDAO::class.java
        ).allowMainThreadQueries().build()
        alertDao = db.getAlertDao()
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun getAlerts_insertFavoriteItem_countOfItems() = runBlockingTest {
        val data = Alert(12323,13652,30.25,30.25,"Alex")
        val data1 = Alert(12323,13652,30.25,30.25,"Cairo")
        val data2 = Alert(12323,13652,30.25,30.25,"Korea")
        val data3 = Alert(12323,13652,30.25,30.25,"Minia")

        alertDao.insertAlert(data)
        alertDao.insertAlert(data1)
        alertDao.insertAlert(data2)
        alertDao.insertAlert(data3)
        //When
        val result = alertDao.getAlerts()
            .first()
        //Then
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(4))
    }

    @Test
    fun insertAlert_insertSingleItem_returnItem() = runBlockingTest {
        val data = Alert(12323,13652,30.25,30.25,"Alex")
        //When
        alertDao.insertAlert(data)
        //Then
        val result = alertDao.getAlerts().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())
    }

    @Test
    fun deleteAlert_deleteItem_checkIsNull() = runBlockingTest {
        val data = Alert(12323,13652,30.25,30.25,"Alex")
        //When
        alertDao.insertAlert(data)
        alertDao.deleteAlert(data)
        val result = alertDao.getAlerts().first()
        assertThat(result, IsEmptyCollection.empty())
        assertThat(result.size, CoreMatchers.`is`(0))

    }
}