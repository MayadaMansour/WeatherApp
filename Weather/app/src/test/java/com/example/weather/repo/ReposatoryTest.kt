package com.example.weather.repo

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.mvvm.Model.Reposatory
import com.example.mvvm.Room.LocalSource
import com.example.weather.MainCoroutineRule
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.localSourceTest.FakeLocalDataSource
import com.example.weather.localSourceTest.FakeRemoteDataSource
import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.CurrentWeather
import com.example.weather.models.MyResponce
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)
class ReposatoryTest {
   @get:Rule
   val rule = InstantTaskExecutorRule()

   @get:Rule
   private lateinit var remoteDataSource: RemoteSourceInterface
   private lateinit var localDataSource: LocalSource
   private lateinit var repository: Reposatory
   lateinit var sharedPreferences: SharedPreferences
   lateinit var weatherResponse: MyResponce
   lateinit var favoriteList: MutableList<City>
   lateinit var alertList: MutableList<Alert>
   lateinit var fakeRemoteDataSource: FakeRemoteDataSource
   lateinit var fakeLocalDataSource: FakeLocalDataSource
   val testingContext: Application = ApplicationProvider.getApplicationContext()
   @ExperimentalCoroutinesApi
   @get:Rule
   var mainCoroutineRule = MainCoroutineRule()
   @Before
   fun getRepo(){
      var current= CurrentWeather(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
         1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
      weatherResponse = MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
      sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(testingContext)
      fakeRemoteDataSource = FakeRemoteDataSource()
      fakeLocalDataSource = FakeLocalDataSource()
      repository = Reposatory(fakeRemoteDataSource,fakeLocalDataSource, sharedPreferences)

   }
   @Test
   fun testRetrofit() = runBlocking{
      val test = weatherResponse
      repository.getWeatherOverNetwork(lat = 34.4, lon = 36.9, exclude = "ex", appid = "a62af663ada4f8dbf13318c557451a3b","en","unit")
         .collect{
            assertEquals(test,it?.body())
         }

   }

   @Test
   fun insertWeathers() = runBlockingTest{
      val item = favoriteList.get(0)
      repository.insertWeathers(item)
      val result = repository.getStoreWeathers().collect{
         assertEquals(it.get(0).city,item.city)
      }

   }

   @Test
   fun deleteWeathers() = runBlocking {
      val item = favoriteList.get(0)
      repository.insertWeathers(item)
      repository.deleteWeathers(item)
      val result = repository.getStoreWeathers().collect{
         assertThat(it,`is`(listOf()))

      }
   }

   @Test
   fun getStoreWeathers() = runBlocking{
      val item = favoriteList.get(0)
      repository.insertWeathers(item)
      repository.getStoreWeathers().collect{
         assertThat(it,`is`(listOf(item,item)))
      }
   }

}




