package com.example.weather.data.weather.LocalSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Dao
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
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DAOTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: WeatherDAO
    private lateinit var alertDao: DAO

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDAO::class.java
        ).allowMainThreadQueries().build()
        alertDao = db.getWeathersDao()
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun getAll_insertFavoriteItem_countOfItems() = runBlockingTest {
        val data = City(123.0, 321.5, "Alex")
        val data1 = City(123.0, 321.5, "Ciro")
        val data2 = City(123.0, 321.5, "Korea")
        val data3 = City(123.0, 321.5, "Minia")

        alertDao.insert(data)
        alertDao.insert(data1)
        alertDao.insert(data2)
        alertDao.insert(data3)
        //When
        val result = alertDao.getAll()
            .first()
        //Then
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(4))
    }

    @Test
    fun insert_insertSingleItem_returnItem() = runBlockingTest {
        val data = City(123.0, 321.5, "Alex")
        //When
        alertDao.insert(data)
        //Then
        val result = alertDao.getAll().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())
    }

    @Test
    fun delete_deleteItem_checkIsNull() = runBlockingTest {
        val data = City(123.0, 321.5, "Alex")
        //When
        alertDao.insert(data)
        alertDao.delete(data)
        val result = alertDao.getAll().first()
        assertThat(result, IsEmptyCollection.empty())
        assertThat(result.size, `is`(0))

    }
}