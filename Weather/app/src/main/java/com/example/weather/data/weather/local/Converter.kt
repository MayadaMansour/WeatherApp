package com.example.weather.data.weather.local

import androidx.room.TypeConverter
import com.example.weather.models.CurrentWeather
import com.example.weather.models.Daily
import com.example.weather.models.MyResponce
import com.google.gson.Gson


class Converter{
    @TypeConverter
    fun fromCurrentToGson(current: CurrentWeather): String = Gson().toJson(current)

    @TypeConverter
    fun fromGsonToCurrent(string: String): CurrentWeather = Gson().fromJson(string, CurrentWeather::class.java)

    @TypeConverter
    fun fromDailyListToGson(daily: List<Daily>) = Gson().toJson(daily)!!

    @TypeConverter
    fun fromGsonToDailyList(string: String) =
        Gson().fromJson(string, Array<Daily>::class.java).toList()

   @TypeConverter
    fun fromHourlyListToGson(hourly: List<CurrentWeather>) = Gson().toJson(hourly)!!

    @TypeConverter
    fun fromGsonToHourlyList(stringHourly: String) =
        Gson().fromJson(stringHourly, Array<CurrentWeather>::class.java).toList()

  /*  @TypeConverter
    fun fromMinutelyToGson(minutely: List<Minutely>): String = Gson().toJson(minutely)


    @TypeConverter
    fun fromGsonToMinutely(string: String): List<Minutely> =
        Gson().fromJson(string, Array<Minutely>::class.java).toList()


    @TypeConverter
    fun fromAlertToGson(alerts: List<Alerts?>?) = Gson().toJson(alerts)!!

    @TypeConverter
    fun fromGsonToAlert(stringAlert: String?) =
        Gson().fromJson(stringAlert, Array<Alerts?>::class.java)?.toList()*/

}