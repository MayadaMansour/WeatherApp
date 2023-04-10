package com.example.weather.data.weather.LocalSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.getOrAwaitValue
import com.example.weather.models.Alert
import com.example.weather.models.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ConcreteLocalSourceTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var local: ConcreteLocalSource
    lateinit var alert: Alert
    lateinit var city: City

    @Before
    fun setUp() {
        local = ConcreteLocalSource(ApplicationProvider.getApplicationContext())
        alert = Alert(2, 3, 2.2, 2.5, "Alex")
        city = City(2.2, 2.5, "Alex")
    }

    @Test
    fun insert_weather_checkList() = runBlockingTest {
        local.insertWeathers(city)
        var result = local.getStoreWeathers().getOrAwaitValue { }
        Assertions.assertThat(result).contains(city)
        local.deleteWeathers(city)
    }

    @Test
    fun insert_alert_checkList() = runBlockingTest {
        local.insertAlert(alert)
        var result = local.getAlerts().getOrAwaitValue { }
        Assertions.assertThat(result).contains(alert)
        local.deleteAlert(alert)
    }

}