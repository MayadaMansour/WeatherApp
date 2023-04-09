package com.example.weather.data.weather.netwok

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.ui.main.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

class ServicsTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private var servics: Servics?=null

    @Before
    fun setUp() {
        servics = RetrofitHelper.retrofit.create(Servics::class.java)
    }

    @After
    fun tearDown() {
        servics=null
    }

    @Test
    suspend fun getAllWeather_requestKey_Authorized() = runBlockingTest {
        val longitude = 30.0
        val latitude = 25.0
        val language = "en"
        val unit = "metric"
        val apiKey = Constants.APPID

        //When
        val response = servics?.getAllWeather(
            latitude, longitude,"", apiKey,
             language, unit
        )
        //Then
        MatcherAssert.assertThat(response?.code() as Int, Matchers.`is`(latitude))
        MatcherAssert.assertThat(response?.body(), Matchers.notNullValue())

    }

    fun getAllWeather_requestNoKey_unAuthorized() = runBlocking{
        //Given
        val longitude =30.0
        val latitude = 25.0
        val language="en"
        val unit = "metric"
        val apiKey = "null"

        //When
        val response= servics?.getAllWeather(
            latitude, longitude,"", apiKey,
             language, unit
        )

        //Then.
        MatcherAssert.assertThat(response?.code(), Matchers.`is`(4001))
        MatcherAssert.assertThat(response?.body(), IsNull.nullValue())



    }


}