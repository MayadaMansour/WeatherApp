package com.example.weather.repo

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvm.Model.RepoInterface
import com.example.mvvm.Model.Reposatory
import com.example.mvvm.Room.LocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.RemoteSourceInterface
import com.example.weather.localSourceTest.FakeLocalDataSource
import com.example.weather.localSourceTest.FakeRemoteDataSource
import com.example.weather.localSourceTest.FakeReposatory
import com.example.weather.models.Alert
import com.example.weather.models.City
import com.example.weather.models.CurrentWeather
import com.example.weather.models.MyResponce
import com.example.weather.ui.main.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReposatoryTest {

@get:Rule
var instantExecutorRule = InstantTaskExecutorRule()

lateinit var repo : RepoInterface
lateinit var localDataSource: LocalSource
lateinit var remoteDataSource: RemoteSourceInterface
lateinit var sharedPreferences: SharedPreferences


@Before
fun setUp() {
    var current= CurrentWeather(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
        1420225,2654552,25225.0,2525,2145.00, listOf(),null,null)
    var data1= MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
    var data2=MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
    var data3=MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
    var data4=MyResponce(30.0,25.0,"152655",165552455,current, listOf(current),null,null)
    val data = Alert(12323,13652,30.25,30.25,"Alex")
    val data5 =  Alert(12323,13652,30.25,30.25,"cairo")
    val data6 =  Alert(12323,13652,30.25,30.25,"Kora")
    val data7 =  Alert(12323,13652,30.25,30.25,"Minia")
    localDataSource= FakeLocalDataSource(listOf(data,data5,data6,data7)as MutableList<Alert>,
        listOf(data2,data3,data4) as MutableList<City>

    )
    remoteDataSource= FakeRemoteDataSource(repo as FakeReposatory)
    repo =  Reposatory.getInstance( Client.getInstance(),localDataSource,sharedPreferences)
}

@After
fun tearDown() {
}
@Test
fun get_All_weather_return_myResponce()= runBlocking{
    //given
    val latitude=30.0
    val lon=25.0

    //when
    val response= repo.getWeatherOverNetwork(latitude.toDouble(),lon.toDouble(),"exclude",Constants.APPID,"en","unit" ).first()
    //then

    MatcherAssert.assertThat(response.code(), Matchers.`is`(latitude))
    MatcherAssert.assertThat(response.body(), Matchers.notNullValue())
}

}