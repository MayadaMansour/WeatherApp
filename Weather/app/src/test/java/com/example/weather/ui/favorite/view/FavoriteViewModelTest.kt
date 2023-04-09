package com.example.weather.ui.favorite.view

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvm.Room.LocalSource
import com.example.weather.MainCoroutineRule
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.LocalDataState
import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.localSourceTest.FakeLocalDataSource
import com.example.weather.localSourceTest.FakeRemoteDataSource
import com.example.weather.localSourceTest.FakeReposatory
import com.example.weather.models.Alert
import com.example.weather.models.City
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
class FavoriteViewModelTest {

    // Subject under test
    private lateinit var favViewModel: FavoriteViewModel

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
    lateinit var welcomeList: MutableList<City>
    lateinit var alertList: MutableList<Alert>
    lateinit var current1: City
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {

        var data1 = City(30.0, 25.0, "Alex")
        var data2 = City(30.0, 25.0, "Alex")
        var data3 = City(30.0, 25.0, "Cairo")
        var data4 = City(30.0, 25.0, "Korea")
        val data = Alert(12323, 13652, 30.25, 30.25, "Alex")
        val data5 = Alert(12323, 13652, 30.25, 30.25, "Cairo")
        val data6 = Alert(12323, 13652, 30.25, 30.25, "Korea")
        welcomeList = listOf(data1, data2, data3, data4) as MutableList<City>
        alertList = listOf(data, data5, data6) as MutableList<Alert>

        localDataSource = FakeLocalDataSource(
            alertList,
            welcomeList
        )
        remoteDataSource = FakeRemoteDataSource(repository)
        repository = FakeReposatory(remoteDataSource as Client, localDataSource, sharedPreferences)
        favViewModel = FavoriteViewModel(repository)


    }

    @After
    fun tearDown() {
    }


    @Test
    fun get_favoriteWeathe_retur_list_favorites() = runBlockingTest {
        //given
        var favList = welcomeList
        //when
        favViewModel.insertWeathers(current1)
        var result = favViewModel.favoriteWeather.value
        when (result) {
            is LocalDataState.Loading -> {

            }
            is LocalDataState.Success -> {

                favList = result.data as MutableList<City>
            }
            is LocalDataState.Fail -> {

            }

        }
        //Then
        MatcherAssert.assertThat(favList, IsNull.notNullValue())

    }
}







