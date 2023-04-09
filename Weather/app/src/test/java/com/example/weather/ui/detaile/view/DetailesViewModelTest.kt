package com.example.weather.ui.detaile.view

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvm.Room.LocalSource
import com.example.weather.MainCoroutineRule
import com.example.weather.data.weather.netwok.ApiState
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.localSourceTest.FakeRemoteDataSource
import com.example.weather.localSourceTest.FakeReposatory
import com.example.weather.models.Alert
import com.example.weather.models.CurrentWeather
import com.example.weather.models.MyResponce
import com.example.weather.ui.home.view.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

@ExperimentalCoroutinesApi
class DetailesViewModelTest {
    private lateinit var detailesViewModel: DetailesViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeReposatory

    // Executes each task synchronously using Architecture Components.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var localDataSource: LocalSource
    lateinit var remoteDataSource: RemoteSourceInterface
    lateinit var welcomeList: MutableList<MyResponce>
    lateinit var alertList: MutableList<Alert>
    lateinit var current1: MyResponce
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        var current= CurrentWeather(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
            1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
        current1=  MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
        var data2=  MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
        var data3=  MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
        var data4=  MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
        val data = Alert(12323,13652,30.25,30.25,"Ismailia")
        val data5 =  Alert(12323,13652,30.25,30.25,"cairo")
        val data6 =  Alert(12323,13652,30.25,30.25,"suez")
        val data7 =  Alert(12323,13652,30.25,30.25,"port said")
        welcomeList=listOf(data2,data3,data4) as MutableList<MyResponce>
        alertList=listOf(data,data5,data6,data7)as MutableList<Alert>

        remoteDataSource = FakeRemoteDataSource(repository)
        repository = FakeReposatory(remoteDataSource as Client, localDataSource, sharedPreferences)
        detailesViewModel = DetailesViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun get_currentWeather() = runBlockingTest {
        //given
        var lat=30.0
        var lon=25.0
        var data:MyResponce = current1
        //when
        detailesViewModel.getWeatherFromApi(current1.lat,current1.lon,"","","","")
        var result = detailesViewModel.currentWeather.value
        when (result) {
            is ApiState.Loading -> {

            }
            is ApiState.Success -> {
                data = result.data.body()!!
            }
            is ApiState.Fail -> {

            }
        }
        //Then
        MatcherAssert.assertThat(data, IsNull.notNullValue())
    }

    @Test
    fun set_currentWeather() {
    }

    @Test
    fun getCurrentWeather() {
    }

    @Test
    fun setCurrentWeather() {
    }

    @Test
    fun getWeatherFromApi() {
    }
}