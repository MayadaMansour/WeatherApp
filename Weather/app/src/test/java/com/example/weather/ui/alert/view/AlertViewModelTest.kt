package com.example.weather.ui.alert.view

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvm.Room.LocalSource
import com.example.weather.MainCoroutineRule
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.LocalDataState
import com.example.weather.data.weather.netwok.LocalDataStateAlerts
import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.getOrAwaitValue
import com.example.weather.localSourceTest.FakeLocalDataSource
import com.example.weather.localSourceTest.FakeRemoteDataSource
import com.example.weather.localSourceTest.FakeReposatory
import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.CurrentWeather
import com.example.weather.models.MyResponce
import com.example.weather.ui.favorite.view.FavoriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AlertViewModelTest {
    // Subject under test
    private lateinit var alertViewModel: AlertViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeReposatory

    // Executes each task synchronously using Architecture Components.
    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var current1: Alert
    lateinit var weatherResponse: MyResponce


    @Before
    fun setUp() {

        var curr= CurrentWeather(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
            1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
        weatherResponse = MyResponce(30.0,25.0,"152655",165552455,curr, listOf(curr),null,null)
        current1 = Alert(12323, 13652, 30.25, 30.25, "Alex")
        var data2 = City(30.0, 25.0, "Alex")
        var data3 = City(30.0, 25.0, "Cairo")
        var data4 = City(30.0, 25.0, "Korea")
        val data = Alert(12323, 13652, 30.25, 30.25, "Alex")
        val data5 = Alert(12323, 13652, 30.25, 30.25, "Cairo")
        val data6 = Alert(12323, 13652, 30.25, 30.25, "Korea")
        repository= FakeReposatory()
        alertViewModel = AlertViewModel(repository)


    }
    @Test
    fun insert_alert_Item_CheckNotEmpty () = runBlockingTest {
        alertViewModel.insertAlertDB(current1)
        var result = alertViewModel._currentAlert.getOrAwaitValue {  } as LocalDataState.Success
        assertThat(result.data, CoreMatchers.`is`(CoreMatchers.not(CoreMatchers.nullValue())))


    }

    @Test
    fun insert_alert_Item_andDeleteIt () = runBlockingTest {
        alertViewModel.insertAlertDB(current1)
        alertViewModel.deleteAlertDB(current1)
        var result = alertViewModel._currentAlert.getOrAwaitValue {  } as LocalDataState.Success
        assertThat(result.data, CoreMatchers.`is`(emptyList()))

    }

    @Test
    fun insert_alert_Item_andcheckGetFavorite () = runBlockingTest {
        alertViewModel.insertAlertDB(current1)
        alertViewModel.getAlertsDB()
        var result = alertViewModel._currentAlert.getOrAwaitValue {  } as LocalDataState.Success
        assertThat(result.data, CoreMatchers.`is`(CoreMatchers.not(CoreMatchers.nullValue())))

    }

}